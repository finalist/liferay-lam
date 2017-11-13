package nl.finalist.liferay.lam.api;

public interface Tag {

    void createTag(String name, String siteFriendlyURL);
    void deleteTag(String name, String siteFriendlyURL);
}
