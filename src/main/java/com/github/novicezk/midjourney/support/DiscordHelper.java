package com.github.novicezk.midjourney.support;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.PrimitiveArrayUtil;
import com.github.novicezk.midjourney.ProxyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscordHelper {
	private final ProxyProperties properties;
	private static final char[] REGEX_SPECIAL_CHARS = new char[]{'.', '*', '+', '?', '^', '$', '[', ']', '|', '(', ')'};
	/**
	 * DISCORD_SERVER_URL.
	 */
	public static final String DISCORD_SERVER_URL = "https://discord.com";
	/**
	 * DISCORD_CDN_URL.
	 */
	public static final String DISCORD_CDN_URL = "https://cdn.discordapp.com";
	/**
	 * DISCORD_WSS_URL.
	 */
	public static final String DISCORD_WSS_URL = "wss://gateway.discord.gg";

	public String getServer() {
		if (CharSequenceUtil.isBlank(this.properties.getNgDiscord().getServer())) {
			return DISCORD_SERVER_URL;
		}
		String serverUrl = this.properties.getNgDiscord().getServer();
		if (serverUrl.endsWith("/")) {
			serverUrl = serverUrl.substring(0, serverUrl.length() - 1);
		}
		return serverUrl;
	}

	public String getCdn() {
		if (CharSequenceUtil.isBlank(this.properties.getNgDiscord().getCdn())) {
			return DISCORD_CDN_URL;
		}
		String cdnUrl = this.properties.getNgDiscord().getCdn();
		if (cdnUrl.endsWith("/")) {
			cdnUrl = cdnUrl.substring(0, cdnUrl.length() - 1);
		}
		return cdnUrl;
	}

	public String getWss() {
		if (CharSequenceUtil.isBlank(this.properties.getNgDiscord().getWss())) {
			return DISCORD_WSS_URL;
		}
		String wssUrl = this.properties.getNgDiscord().getWss();
		if (wssUrl.endsWith("/")) {
			wssUrl = wssUrl.substring(0, wssUrl.length() - 1);
		}
		return wssUrl;
	}

	public String generateFinalPrompt(String id, String prompt) {
		String idPrefix = this.properties.getDiscord().getIdPrefix();
		String idSuffix = this.properties.getDiscord().getIdSuffix();
		return idPrefix + id + idSuffix + " " + prompt;
	}

	public String findTaskIdByFinalPrompt(String finalPrompt) {
		String idPrefix = this.properties.getDiscord().getIdPrefix();
		String idSuffix = this.properties.getDiscord().getIdSuffix();
		return CharSequenceUtil.subBetween(finalPrompt, idPrefix, idSuffix);
	}

	public String convertContentRegex(String contentRegex) {
		String prefix = convertRegex(this.properties.getDiscord().getIdPrefix());
		String suffix = convertRegex(this.properties.getDiscord().getIdSuffix());
		return contentRegex.replaceFirst("<", prefix).replaceFirst(">", suffix);
	}

	private static String convertRegex(String regex) {
		char[] chars = regex.toCharArray();
		Character[] characters = new Character[chars.length];
		for (int i = 0; i < chars.length; i++) {
			characters[i] = chars[i];
		}
		List<Character> charList = ListUtil.toList(characters);
		for (int i = 0; i < charList.size(); i++) {
			if (PrimitiveArrayUtil.contains(REGEX_SPECIAL_CHARS, charList.get(i))) {
				charList.add(i, '\\');
				charList.add(i, '\\');
				i += 2;
			}
		}
		StringBuilder sb = new StringBuilder();
		charList.forEach(sb::append);
		return sb.toString();
	}

}
