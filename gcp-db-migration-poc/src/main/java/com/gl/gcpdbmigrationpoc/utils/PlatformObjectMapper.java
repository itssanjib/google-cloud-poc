package com.gl.gcpdbmigrationpoc.utils;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gl.gcpdbmigrationpoc.exception.ServiceException;
import com.google.gson.Gson;

public class PlatformObjectMapper {

	/** The object mapper for jackson serialised objects. */
	private static ObjectMapper objectMapper = new ObjectMapper();
	/** The object mapper for GSON serialised objects. */
	private static Gson gson = new Gson();

	static {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(PlatformObjectMapper.class);

	/**
	 * Instantiates a new platform object mapper.
	 */
	private PlatformObjectMapper() {
		super();
	}

	public static <T> T gsonToObject(String json, Class<T> clazz) {
		try {
			return gson.fromJson(json, clazz);
		} catch (final Exception ex) {
			log.error("Exception while converting JSON to object {} {}", json, ex);
		}
		return null;
	}

	/**
	 * Map to object.
	 *
	 * @param <T>        the generic type
	 * @param jsonObject the json object
	 * @param clazz      the clazz
	 * @return the t the platform exception
	 */

	public static <T> T mapToObject(Object jsonObject, Class<T> clazz) {
		try {
			if (jsonObject instanceof Map) {
				return objectMapper.readValue(objectMapper.writeValueAsString(jsonObject), clazz);
			}
			return objectMapper.readValue(jsonObject.toString(), clazz);
		} catch (final Exception ex) {
			log.error("Exception occured during json parsing {}", ex);
			return null;
		}
	}

	/**
	 * Read from file.
	 *
	 * @param path the path
	 * @return the string
	 * @throws IOException
	 */
	public static String readFromFile(String path) throws IOException {
		String inputParams = "";
		try (Scanner scanner = new Scanner(PlatformObjectMapper.class.getClassLoader().getResourceAsStream(path))) {
			inputParams = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
			throw new IOException();
		}
		return inputParams;
	}

	/**
	 * Map to json.
	 *
	 * @param jsonAsObject the json as object
	 * @return the string
	 * @throws ServiceException
	 * @throws JsonProcessingException
	 * @throws PlatformException       the platform exception
	 */
	public static String mapToJson(Object jsonAsObject) throws ServiceException {
		try {
			return objectMapper.writeValueAsString(jsonAsObject);
		} catch (JsonProcessingException e) {
			throw new ServiceException("");
		}
	}

	/**
	 * String to JsonNode
	 *
	 * @param jsonString
	 * @return JsonNode
	 */
	public static JsonNode mapStringToJsonNode(String jsonString) throws ServiceException {
		try {
			return objectMapper.readTree(jsonString);
		} catch (IOException e) {
			log.error("Error while converting jsonString to JsonNode" + e);
			throw new ServiceException(
					"Error while converting jsonString to JsonNode with with msg ::" + e.getMessage());
		}
	}
	
	/**
	   * Creates Object of type {@code clazz} from {@code classPath} JSON
	   *
	   * @param classPath
	   *          the reference to JSON's classpath
	   * @param clazz
	   *          the reference to expected return type
	   * @return Object of type {@code clazz}
	   * @throws IOException
	   *           in case of failure to get {@link java.io.InputStream} from
	   *           {@code classPath}
	   */
	  public static <T> T fromClassPath(final String classPath, final Class<T> clazz) throws ServiceException {

	    try {
	      return objectMapper.readValue(new ClassPathResource(classPath).getInputStream(), clazz);
	    } catch (IOException e) {
	      throw new ServiceException(e.getMessage());
	    }
	  }
}