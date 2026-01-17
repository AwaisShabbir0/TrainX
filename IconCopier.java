import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class IconCopier {
    public static void main(String[] args) {
        String[] files = { "wallet_icon.png", "food_icon.png", "profile_icon.png" };
        String srcDir = "d:/java-Airline-/src/airlinemanagementsystem/icons/";
        String destDir = "d:/java-Airline-/build/classes/airlinemanagementsystem/icons/";

        for (String file : files) {
            Path src = Paths.get(srcDir + file);
            Path dest = Paths.get(destDir + file);

            try {
                System.out.println("Copying " + src + " to " + dest);
                if (Files.exists(src)) {
                    Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Success: " + file);
                } else {
                    System.err.println("Error: Source file not found: " + src);
                }
            } catch (IOException e) {
                System.err.println("Failed to copy " + file);
                e.printStackTrace();
            }
        }
    }
}
