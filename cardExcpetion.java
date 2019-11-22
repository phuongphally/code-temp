package com.mbanq.card.exception;

import static com.mbanq.card.exception.CardException.*;

public class CardExceptionBuilder {

   private CardExceptionType type;
   private String message;

   private CardExceptionBuilder(){ }

   public static CardExceptionBuilder newInvalidNetWork() {
       return new CardExceptionBuilder()
               .message(INVALID_NETWORK_MESSAGE)
               .type(CardExceptionType.INVALID_NETWORK);
   }

   public static CardExceptionBuilder newInvalidNumber() {
       return new CardExceptionBuilder()
               .message(INVALID_NUMBER_MESSAGE)
               .type(CardExceptionType.INVALID_NUMBER);
   }

   public static CardExceptionBuilder newInvalidOther() {
       return new CardExceptionBuilder()
               .message(INVALID_OTHER_MESSAGE)
               .type(CardExceptionType.INVALID_OTHER);
   }

   public CardExceptionBuilder message(final String message) {
       this.message = message;
       return this;
   }

   public CardExceptionBuilder type(final CardExceptionType type) {
       this.type = type;
       return  this;
   }

   public CardException build() {
       return new CardException(message, type);
   }

}


package com.mbanq.card.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class CardException extends RuntimeException {

    public enum  CardExceptionType {
        INVALID_NETWORK,
        INVALID_NUMBER,
        INVALID_OTHER
    }

    public static final String INVALID_NETWORK_MESSAGE = "Network is not supported";
    public static final String INVALID_NUMBER_MESSAGE = "Card number is not supported";
    public static final String INVALID_OTHER_MESSAGE = "INVALID OTHER MESSAGE";

    private String message;
    private CardExceptionType type;

    public CardException(final String message,
                         final CardExceptionType type) {
        super(message);
        this.message = message;
        this.type = type;
    }

}



package com.mbanq.card.validation;

import com.mbanq.card.domain.Card;
import com.mbanq.card.domain.CardNetwork;
import com.mbanq.card.exception.CardExceptionBuilder;
import org.apache.commons.lang3.EnumUtils;

import java.util.Set;

public class CardUtils {
    public final static Set<Integer> VISA_VALID_DIGIT_LENGTHS = Set.of(16);
    public final static Set<Integer> MASTERCARD_VALID_DIGIT_LENGTHS = Set.of(16);

    public static void validateCardNumberLength(final Card card, final String primaryAccountNumber) {
        //Allow only validated networks
        if (!EnumUtils.isValidEnum(CardNetwork.class, card.getProduct().getNetwork().name())) {
            throw CardExceptionBuilder.newInvalidNetWork()
                    .build();
        }

        //Validate  VISA
        if (card.getProduct().getNetwork().equals(CardNetwork.VISA) &&
                !VISA_VALID_DIGIT_LENGTHS.contains(primaryAccountNumber.length())) {
            throw CardExceptionBuilder.newInvalidNumber()
                    .build();
        }

        //Validate MasterCard
        if (card.getProduct().getNetwork().equals(CardNetwork.MASTERCARD) &&
                !MASTERCARD_VALID_DIGIT_LENGTHS.contains(primaryAccountNumber.length())
        ) {
            throw CardExceptionBuilder.newInvalidNetWork()
                    .build();
        }

        //Verify only numbers allowed
        if (!primaryAccountNumber.matches("^\\d+$")) {
            throw CardExceptionBuilder.newInvalidNetWork()
                    .build();
        }

    }
}








