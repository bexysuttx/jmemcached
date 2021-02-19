package bexysuttx.jmemcached_server;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bexysuttx.jmemcached_server.impl.JmemcachedServerFactory;

public class CLI {
	private static final Logger LOGGER = LoggerFactory.getLogger(CLI.class);
	private static final List<String> QUIT_CMD = Collections
			.unmodifiableList(Arrays.asList(new String[] { "q", "quit", "exit" }));

	public static void main(String[] args) {
		Thread.currentThread().setName("CLI-main thread");
		try {
			Server server = JmemcachedServerFactory.buildNewServer(null);
			server.start();
			waitForStopCommand(server);
		} catch (Exception e) {
			LOGGER.error("Can't execute cmd: " + e.getMessage(), e);
		}
	}

	private static void waitForStopCommand(Server server) {
		try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
			while (true) {
				String cmd = scanner.nextLine();
				if (QUIT_CMD.contains(cmd)) {
					server.stop();
					break;
				} else {
					LOGGER.error("Undifined cmd: " + cmd + "! To shutdown server please type: q");
				}
			}
		}
	}

}
