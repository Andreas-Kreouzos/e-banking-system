package util

import java.nio.file.Paths

class PropertyReader {

    private static Properties props = new Properties()
    private static Properties gradleProps = new Properties()

    static def loadProperties(){
        InputStream is = PropertyReader.class.getResourceAsStream('/test.properties')
        props.load(is)
    }

    static def loadGradleProperties() throws Exception {
        String rootPath = Paths.get(System.getProperty('user.dir'), '..').normalize().toString()
        File gradlePropertiesFile = new File(rootPath, 'gradle.properties')

        gradleProps.load(new FileInputStream(gradlePropertiesFile))
    }

    static String getProperty(String key){
        if(props.isEmpty()) loadProperties()
        props.getProperty(key)
    }

    static String getGradleProperty(String key) {
        if (gradleProps.isEmpty()) loadGradleProperties()
        gradleProps.getProperty(key)
    }
}
