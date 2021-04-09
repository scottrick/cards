import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.*

class Keystore(
    project: Project
) {
    //load the release keystore info from keystore.properties
    private val keystorePropertiesFile: File? = project.file("keystore.properties")
    val properties = Properties()

    init {
        if (keystorePropertiesFile?.exists() == true) {
            properties.load(FileInputStream(keystorePropertiesFile))
        } else {
            println("WARNING: keystore.properties file is missing.")
        }
    }
}