package org.tinkernut.apririce.commands;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.tinkernut.apririce.textUtils.Parser;
import org.tinkernut.apririce.textUtils.TextBuffer;

public class HashCommand extends Command{
	private final String helpText = "Available hashing algorithms: MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512. Format like so : |hash (algorithm) (text to hash) --- eg. |hash sha1 help";
	private MessageDigest mDigest;

	@Override
	void exec() {
		if (Parser.stripArguments(params).equals("")) {
			TextBuffer.addAndDisplay(helpText, me);
		} else {
			String hashingAlgorithm = Parser.getArgument(params, 1);			
			
			try {
				mDigest = MessageDigest.getInstance(hashingAlgorithm);
				mDigest.update(Parser.stripArguments(params).getBytes());
				TextBuffer.addAndDisplay(new BigInteger(1, mDigest.digest()).toString(16), me);
			} catch (NoSuchAlgorithmException e) {
				TextBuffer.addAndDisplay("No such hashing algorithm.", me);
			}
		}
	}

	@Override
	void execPriv() {
		exec();
	}

}
