package ru.popov.bodya.eventsmanager.model;


public class EditModeHolder {

    public enum EditMode {
        Update,
        Delete,
        Observe
    }

    private EditMode editMode;

    public EditModeHolder(EditMode editMode) {
        this.editMode = editMode;
    }

    public EditMode getEditMode() {
        return editMode;
    }

    public void setEditMode(EditMode editMode) {
        this.editMode = editMode;
    }
}
