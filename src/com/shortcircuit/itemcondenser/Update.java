package com.shortcircuit.itemcondenser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;

public class Update {
    final static String drive = "https://dl.dropboxusercontent.com/s/r9kwllf3clfoz8f/ItemCondenser.txt?dl=1&token_hash=AAFNc9WxD_XpgnJWhiTkmato0xqn5mORQrXNJR_4Cgd2zA";
    public static double getCurrentVersion() throws MalformedURLException, IOException, NumberFormatException{
        URL url = new URL(drive);
        Scanner scan = new Scanner(url.openStream());
        String line = scan.next();
        scan.close();
        return Double.parseDouble(line);
    }
    public static void download(String url, String file) throws MalformedURLException, IOException{
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
    }
}
