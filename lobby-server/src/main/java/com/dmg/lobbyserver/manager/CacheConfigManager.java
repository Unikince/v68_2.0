package com.dmg.lobbyserver.manager;

import com.zyhy.common_server.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class CacheConfigManager {

	private static CacheConfigManager instance = new CacheConfigManager();


	//铭感字
	private Map sensitiveWordMap = new HashMap();

	private CacheConfigManager() {

	}

	public static CacheConfigManager instance() {
		return instance;
	}

	public void init() {
		try {
			initKeyWord();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Set<String> readSensitiveWordFile() throws Exception{
		Set<String> sensitiveWords = new HashSet<>();
		InputStreamReader read = new InputStreamReader(this.getClass().getResourceAsStream("/keyword.txt"), "UTF-8");
		try {
			BufferedReader bufferedReader = new BufferedReader(read);
			String txt = null;
			while((txt = bufferedReader.readLine()) != null){
				sensitiveWords.add(txt);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			read.close();
		}
		return sensitiveWords;
	}

	public boolean isContaintSensitiveWord(String txt){
		boolean flag = false;
		for(int i = 0 ; i < txt.length() ; i++){
			int matchFlag = this.CheckSensitiveWord(txt, i, 1);
			if(matchFlag > 0){
				flag = true;
			}
		}
		return flag;
	}

	public Set<String> getSensitiveWord(String txt , int matchType){
		Set<String> sensitiveWordList = new HashSet<String>();

		for(int i = 0 ; i < txt.length() ; i++){
			int length = CheckSensitiveWord(txt, i, matchType);
			if(length > 0){
				sensitiveWordList.add(txt.substring(i, i+length));
				i = i + length - 1;
			}
		}
		return sensitiveWordList;
	}

	public String replaceSensitiveWord(String txt,int matchType,String replaceChar){
		String resultTxt = txt;
		Set<String> set = getSensitiveWord(txt, matchType);
		Iterator<String> iterator = set.iterator();
		String word = null;
		String replaceString = null;
		while (iterator.hasNext()) {
			word = iterator.next();
			replaceString = getReplaceChars(replaceChar, word.length());
			resultTxt = resultTxt.replaceAll(word, replaceString);
		}
		return resultTxt;
	}

	private String getReplaceChars(String replaceChar,int length){
		String resultReplace = replaceChar;
		for(int i = 1 ; i < length ; i++){
			resultReplace += replaceChar;
		}

		return resultReplace;
	}

	@SuppressWarnings({ "rawtypes"})
	public int CheckSensitiveWord(String txt,int beginIndex,int matchType){
		boolean  flag = false;
		int matchFlag = 0;
		char word = 0;
		Map nowMap = sensitiveWordMap;
		for(int i = beginIndex; i < txt.length() ; i++){
			word = txt.charAt(i);
			String str = String.valueOf(word);
			if (!StringUtils.checkZhongWen(str)) {
				matchFlag++;
				continue;
			}
			nowMap = (Map) nowMap.get(word);
			if(nowMap != null){
				matchFlag++;
				if("1".equals(nowMap.get("isEnd"))){
					flag = true;
					if(1 == matchType){
						break;
					}
				}
			}
			else{
				break;
			}
		}
		if(matchFlag < 2 || !flag){
			matchFlag = 0;
		}
		return matchFlag;
	}

	@SuppressWarnings("rawtypes")
	public Map initKeyWord(){
		try {
			Set<String> keyWordSet = readSensitiveWordFile();
			addSensitiveWordToHashMap(keyWordSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensitiveWordMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
		sensitiveWordMap = new HashMap(keyWordSet.size());
		String key = null;
		Map nowMap = null;
		Map<String, String> newWorMap = null;
		Iterator<String> iterator = keyWordSet.iterator();
		while(iterator.hasNext()){
			key = iterator.next();
			nowMap = sensitiveWordMap;
			for(int i = 0 ; i < key.length() ; i++){
				char keyChar = key.charAt(i);
				Object wordMap = nowMap.get(keyChar);

				if(wordMap != null){
					nowMap = (Map) wordMap;
				}
				else{
					newWorMap = new HashMap<String,String>();
					newWorMap.put("isEnd", "0");
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}

				if(i == key.length() - 1){
					nowMap.put("isEnd", "1");
				}
			}
		}
	}

}
