package yalp.data.validation;

import yalp.i18n.MessagesBuilder;

public class ValidationBuilder {

    public static void build() {
        new MessagesBuilder().build();
        Validation.current.set(new Validation());
    }
}
