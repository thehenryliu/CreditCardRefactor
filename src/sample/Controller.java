package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Controller {

    String inputCardNumber = "";
    String inputExpiration = " ";
    String inputCcv = " ";
    String creditCardType = "unknown";
    Image ccImgVisa = new Image(getClass().getResourceAsStream("visa.png"));
    Image ccImgMastercard = new Image(getClass().getResourceAsStream("mastercard.png"));
    Image ccImgAmex = new Image(getClass().getResourceAsStream("amex.png"));
    Image ccImgJcb = new Image(getClass().getResourceAsStream("jcb.png"));
    Image ccImgUnknown = new Image(getClass().getResourceAsStream("unknowncard.png"));
    Image validationCheckMark = new Image(getClass().getResourceAsStream("validationPassed.png"));
    Image validationX = new Image(getClass().getResourceAsStream("validationFailed.png"));


    @FXML // fx:id="creditCardNumTextField";
    private TextField creditCardNumTextField;

    @FXML //fx:id="creditCardImageView";
    private ImageView creditCardImageView;

    @FXML //fx:id="validationResultCreditCardNum"
    private ImageView validationResultCreditCardNum;

    @FXML //fx:id="expirationTextField"
    private TextField expirationTextField;

    @FXML //fx:id="validationResultExpiration"
    private ImageView validationResultExpiration;

    @FXML //fx:id="expirationTextField"
    private TextField ccvTextField;

    @FXML //fx:id="validationResultCcv
    private ImageView validationResultCcv;

    public void ccvKeyReleased (Event event)
    {
        inputCcv = ccvTextField.getText();

        if (creditCardType.equals("amex"))
        {
            if (inputCcv.length() < 4)
                validationResultCcv.setVisible(false);
            else if (inputCcv.length() == 4 && inputCcv.matches("[0-9.]+"))
                showValidationMark ("ccv", "passed");
            else
                showValidationMark ("ccv", "failed");
        }
        else if (creditCardType.equals("unknown")) //card unknown, no fails unless CCV is 5 or more characters or contains letters
        {
            if (inputCcv.length() < 5)
                validationResultCcv.setVisible(false);
            if (inputCcv.length() >=5)
                showValidationMark ("ccv", "failed");
        }
        else {
            if (inputCcv.length() < 3)
                validationResultCcv.setVisible(false);
            else if (inputCcv.length() == 3 && inputCcv.matches("[0-9.]+"))
                showValidationMark ("ccv", "passed");
            else
                showValidationMark ("ccv", "failed");
        }
    }

    public void expirationKeyReleased (Event event)
    {
        inputExpiration = expirationTextField.getText();

        if (inputExpiration.length() < 7)
        {
            validationResultExpiration.setVisible(false);
        }
        else if (inputExpiration.length() > 7)
        {
            showValidationMark ("expiration", "failed");
        }
        else
        {
            String month = inputExpiration.substring(0,2);
            String year = inputExpiration.substring(3);

            if (inputExpiration.charAt(2) == '/')
            {
                if (validMonth(month) && validYear(year))
                {
                    showValidationMark ("expiration", "passed");
                }
                else
                {
                    showValidationMark ("expiration", "failed");
                }
            }
            else
            {
                showValidationMark ("expiration", "failed");
            }
        }
    }

    public void keyReleased(Event event)
    {
        inputCardNumber = this.creditCardNumTextField.getText();

        if(inputCardNumber.length() == 0) { setCreditCardType("unknown"); }

        if (inputCardNumber.length() == 1)
        {
            if (inputCardNumber.charAt(0) == '4')  { setCreditCardType("visa"); }
            else if (inputCardNumber.charAt(0) == '5')  { setCreditCardType("mastercard"); }
            else  { setCreditCardType("unknown"); } // needed to set a card to unknown if it identifies as Amex or JCB and a character is deleted
        }

        if (inputCardNumber.length() >= 2 && inputCardNumber.charAt(0) != '4' && inputCardNumber.charAt(0) != '5') // 2 or more numbers and we know it's not Visa or Mastercard
        {
            if (inputCardNumber.charAt(0) != '3')  { setCreditCardType ("unknown"); } // any number besides 3 is unknown
            else
            {
                if (inputCardNumber.charAt(1) == '4' || inputCardNumber.charAt(1) == '7') // starting with 34 or 37 means American Express
                {
                    setCreditCardType("amex");
                }
                else if (inputCardNumber.charAt(1) == '5') { setCreditCardType("jcb"); } // starting with 35 is JCB
                else { setCreditCardType("unknown"); } // any other 3_ starting digits is unknown
            }
        }


        if (inputCardNumber.length() < 16) //16 would be for numbers only. With spaces it would be 19
        {
            validationResultCreditCardNum.setVisible(false);
        }
        else
        {
            if (inputCardNumber.length() == 16)
            {
                if (creditCardType.equals("unknown"))  { showValidationMark("number", "failed"); }
                else { showValidationMark("number", "passed"); }
            }
            else
            {
                showValidationMark("number", "failed");
            }
        }
    }

    public void setCreditCardType (String cardType)
    {
        creditCardType = cardType;
        switch(cardType)
        {
            case "unknown":
                creditCardImageView.setImage(ccImgUnknown);
                break;
            case "visa":
                creditCardImageView.setImage(ccImgVisa);
                break;
            case "mastercard":
                creditCardImageView.setImage(ccImgMastercard);
                break;
            case "amex":
                creditCardImageView.setImage(ccImgAmex);
                break;
            case "jcb":
                creditCardImageView.setImage(ccImgJcb);
                break;
            default:
                creditCardImageView.setImage(ccImgUnknown);
        }
    }

    public void showValidationMark(String category, String result)
    {
        if (category.equals("number"))
        {
            if (result.equals("passed")) { validationResultCreditCardNum.setImage(validationCheckMark); }
            else { validationResultCreditCardNum.setImage(validationX); }

            validationResultCreditCardNum.setVisible(true);
        }

        if (category.equals("expiration"))
        {
            if (result.equals("passed")) { validationResultExpiration.setImage(validationCheckMark); }
            else { validationResultExpiration.setImage(validationX); }

            validationResultExpiration.setVisible(true);
        }

        if (category.equals("ccv"))
        {
            if (result.equals("passed")) { validationResultCcv.setImage(validationCheckMark); }
            else { validationResultCcv.setImage(validationX); }

            validationResultCcv.setVisible(true);
        }
    }

    public boolean validMonth (String month)
    {
        if (month.matches("[0-9.]+") && month.length() == 2)
        {
            if (Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 12)
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public boolean validYear (String year)
    {
        return year.matches("[0-9.]+") && year.length() == 4;
    }
}
