package com.company;

import java.io.Serializable;
import java.util.Date;

/**
 * This class implement abstract "Serializable" and also represent a note that include title, context or text and date.
 * @author Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since May.14.2020
 */
public class Note implements Serializable {
    private String title;
    private String text;
    private Date date;

    /**
     * This is constructor of this class and fill our fields.
     * @param title is title of a note
     * @param text is text of a note
     * @param date is date that note was created
     */
    public Note(String title, String text, Date date){
        this.title=title;
        this.text=text;
        this.date=date;
    }

    /**
     * This is setter method for text field.
     * @param text is a text for setting in as context of note
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Getter method of title field
     * @return title of note
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter method of text field
     * @return text of note
     */
    public String getText() {
        return text;
    }

    /**
     * Getter method of date field
     * @return date of note
     */
    public Date getDate() {
        return date;
    }
}
