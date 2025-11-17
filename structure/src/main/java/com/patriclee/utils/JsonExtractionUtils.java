package com.patriclee.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * Extracts JSON payloads from free-form text such as LLM responses.
 */
public final class JsonExtractionUtils {

    private static final ObjectMapper STRICT_MAPPER = new ObjectMapper();
    private static final ObjectMapper LENIENT_MAPPER = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
            .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
            .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
            .build();

    private static final Pattern RESULT_BLOCK_PATTERN = Pattern.compile(
            "<result>(.*?)</result>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private static final Pattern CODE_FENCE_PATTERN = Pattern.compile(
            "^\\s*```(?:json)?\\s*(.*?)\\s*```\\s*$",
            Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private static final Pattern NONE_PATTERN = Pattern.compile("(?<![\"\\w])None(?![\\w\"])");
    private static final Pattern NON_VISIBLE_WHITESPACE = Pattern.compile(
            "[\\u00A0\\u1680\\u2000-\\u200B\\u202F\\u205F\\u3000\\uFEFF]");

    private JsonExtractionUtils() {
    }

    /**
     * Extracts the best-effort JSON text and parsed node from raw LLM text.
     *
     * @param rawInput raw LLM text
     * @return extraction result
     */
    public static ExtractionResult extract(String rawInput) {
        String payload = rawInput == null ? "" : rawInput;
        payload = unwrapArgWrapper(payload);
        payload = extractResultBlock(payload);
        payload = stripCodeFences(payload.trim());
        payload = normalizeWhitespace(payload);
        String cleaned = payload.trim();
        String normalized = normalizeNoneTokens(cleaned);

        JsonNode parsed = tryParse(normalized);
        if (parsed != null && parsed.isArray() && parsed.size() == 1 && parsed.get(0).isObject()) {
            parsed = parsed.get(0);
        }

        String compactJson = normalizeForFallback(normalized);
        if (parsed != null) {
            try {
                compactJson = STRICT_MAPPER.writeValueAsString(parsed);
            } catch (JsonProcessingException ignored) {
                compactJson = normalizeForFallback(normalized);
            }
        }

        return new ExtractionResult(compactJson, parsed);
    }

    private static String unwrapArgWrapper(String payload) {
        try {
            JsonNode node = LENIENT_MAPPER.readTree(payload);
            if (node.isObject() && node.has("arg1")) {
                JsonNode argNode = node.get("arg1");
                if (argNode == null || argNode.isNull()) {
                    return "";
                }
                return argNode.isTextual() ? argNode.asText() : argNode.toString();
            }
        } catch (Exception ignored) {
        }
        return payload;
    }

    private static String extractResultBlock(String payload) {
        Matcher matcher = RESULT_BLOCK_PATTERN.matcher(payload);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return payload;
    }

    private static String stripCodeFences(String payload) {
        Matcher matcher = CODE_FENCE_PATTERN.matcher(payload);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return payload;
    }

    private static String normalizeWhitespace(String payload) {
        if (payload.startsWith("\uFEFF")) {
            payload = payload.substring(1);
        }
        return NON_VISIBLE_WHITESPACE.matcher(payload).replaceAll(" ").trim();
    }

    private static String normalizeNoneTokens(String payload) {
        return NONE_PATTERN.matcher(payload).replaceAll("null");
    }

    private static JsonNode tryParse(String payload) {
        if (payload == null || payload.isBlank()) {
            return null;
        }
        try {
            return LENIENT_MAPPER.readTree(payload);
        } catch (Exception first) {
            try {
                return STRICT_MAPPER.readTree(payload);
            } catch (Exception ignored) {
                return null;
            }
        }
    }

    private static String normalizeForFallback(String payload) {
        return payload == null ? "" : payload.replaceAll("\\s+", "");
    }

    /**
     * Result of a JSON extraction attempt.
     *
     * @param jsonText compacted JSON text or fallback string
     * @param jsonNode parsed JsonNode; {@code null} if parsing failed
     */
    public record ExtractionResult(String jsonText, JsonNode jsonNode) {
    }
}
