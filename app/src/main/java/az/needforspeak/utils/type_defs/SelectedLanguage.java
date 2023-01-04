package az.needforspeak.utils.type_defs;

import androidx.annotation.StringDef;

import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

/**
 * Created by Namig Gadirov
 */
public class SelectedLanguage {
    //Constants
    public static final String AZ = "az";
    public static final String EN = "en";
    public static final String RU = "ru";

    //Bundle items of statuses
    @Retention(AnnotationRetention.SOURCE)
    @StringDef({AZ, EN, RU})
    public @interface LanguageDef {
    }
}
