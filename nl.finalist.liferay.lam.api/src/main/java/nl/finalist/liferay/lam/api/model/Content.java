package nl.finalist.liferay.lam.api.model;

import java.util.LinkedHashMap;

public class Content {
	private String siteKey;
	private String articleId;
	
	public Content(LinkedHashMap<String, Object> map) {
		this.siteKey = (String)map.get("siteKey");
		this.articleId = (String)map.get("articleId");
	}
	
	public String getSiteKey() {
		return siteKey;
	}
	public void setSiteKey(String siteKey) {
		this.siteKey = siteKey;
	}
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
}
