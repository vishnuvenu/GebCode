package Util

import groovy.json.JsonSlurper

/**
 * @author FOR-SEC-SWU
 */
class CredentialUtils {

  private static final String JSON_URL = System.getProperty(BaseSpec.propLssConfigfile, (String)BaseSpec.gebconfig.credentialFile)

  static Map JSON_RESULT = null

  /**
   * Get test property from DB and system property
   * If no DB value and no system property value, return defaultvalue
   * If DB value is presented, return DB value
   * If system property value is presented, return system property value
   * To replace value by system property, use -Dseco.key=value
   *
   * @param key key of the property
   * @param defaultValue default value of the property
   * @return property value
   */
  public static String getTestProperty(String key, String defaultValue) {
    System.getProperty(key, defaultValue)
  }

  /**
   * Get LSS information from config file and system property
   * If no config value and no system property value, return defaultvalue
   * If config value is presented, return config value
   * If system property value is presented, return system property value
   * To replace value by system property, use -Dseco.lss.key.type=value
   * possible key: username, password, officeid, sitecode
   *
   * @param key the key of the LSS information, by default is "default"
   * @param type type for the information, possible type: username, password, officeid, sitecode
   * @param defaultvalue default value of this key
   * @return LSS information
   */
  private static String getLSS(String type, String key = "default", String defaultValue = null) {
    def local = System.getProperty(BaseSpec.propLsslocal, "false")
    key = System.getProperty(BaseSpec.propLss, key)
    if (JSON_RESULT == null) {
      if (local == "true") {
        JSON_RESULT = getJsonFromLocal()
      } else {
        JSON_RESULT = getJsonFromUrl()
      }
    }
    def value = JSON_RESULT.get(key).get(type)
    value = System.getProperty(BaseSpec.propLss + ".${key}.${type}", (String)value)
    if (value == null) { value = defaultValue }
    if (value == null) throw new Exception("LSS user ${key} didn't exist in DB or it's property '${type}' is not defined." +
        " Try to find: 'key' : '${key}' in your DB or provide a default value for property '${type}'.")
    value
  }

  private static Map getJsonFromUrl() {
    URL url = new URL(JSON_URL)
    InputStream urlStream = null
    Map jsonResult = null
    try {
      urlStream = url.openStream()
      BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream))
      JsonSlurper jsonSlurper = new JsonSlurper()
      Object result = jsonSlurper.parse(reader)

      jsonResult = (Map) result
    } catch (e) {
      jsonResult = (Map)getJsonFromLocal()
    } finally {
      if (urlStream != null) {
        urlStream.close()
      }
    }
    jsonResult
  }

  private static getJsonFromLocal() {
    def file = new File("./src/main/resources/configurations/credentials.json")
    JsonSlurper jsonSlurper = new JsonSlurper()
    jsonSlurper.parseText(file.text)
  }

  /**
   * Get LSS username
   * To replace the DB value by system property, -Dseco.lss.key.username=value
   *
   * @param key key of the LSS, by default is default
   * @return username
   */
  public static String getUsername(String key = "default") {
    getLSS("username", key)
  }

  /**
   * Get LSS password
   * To replace the DB value by system property, -Dseco.lss.key.password=value
   *
   * @param key key of the LSS, by default is default
   * @return password
   */
  public static String getPassword(String key = "default") {
    getLSS("password", key)
  }
}
