import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UrlDownloader {


    public static void main(String[] args) throws InterruptedException {

        System.out.println("downloading a total of " + args.length);
        int counter = 0;

        for (String urlString : args) {


            String[] splitUrlurl = urlString.split("/");
            String destination = "./resources/output/" + splitUrlurl[splitUrlurl.length - 1];


            UrlDownloader program = new UrlDownloader();

            boolean stored = program.storeFile(urlString, destination);
            int year = 2016;
            while (!stored & year > 2011){
                year--;
                stored = program.storeFile(urlString.replace("2016", String.valueOf(year)), destination);

            }

            program.uncompressFile(destination, "./resources/output/");

            counter++;

            System.out.println("downloaded file " + counter);
            TimeUnit.SECONDS.sleep(15);

        }



    }

    public boolean storeFile(String urlString, String destination){
        try {
            String url = urlString;
            URL website = new URL(urlString);
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void downloadWebsiteFile(String url){

    }

    public void uncompressFile(String destination, String destinationFolder){
        try {

            byte[] buffer = new byte[1024];

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(destination));
            //get the zipped file list entry
            ZipEntry ze = null;

            ze = zis.getNextEntry();
            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(destinationFolder + fileName);

                System.out.println("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }


            zis.closeEntry();
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
