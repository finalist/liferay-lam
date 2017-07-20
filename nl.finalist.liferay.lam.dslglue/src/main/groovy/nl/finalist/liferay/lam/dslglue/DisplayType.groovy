package nl.finalist.liferay.lam.dslglue

enum DisplayType {
    CHECKBOX("checkbox"), RADIO("radio"), SELECTION_LIST("selection-list"), TEXT_BOX("text-box")

    String description;

    private DisplayType(String description) {
        description = description;
    }
}
