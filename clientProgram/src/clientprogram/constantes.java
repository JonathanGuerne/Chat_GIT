package clientprogram;

import java.awt.Color;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * @author Jonathan Guerne
 * @date 20 sept. 2015
 */
public class constantes {
    
    StyleContext sc;
    Style defaultStyle;
    Style mainText;
    Style deco;
    Style co;
    Style srvMsg;
    Style perso;
    public constantes(StyleContext sc){
        this.sc = sc;
        if(sc!= null){
            
            defaultStyle= sc.getStyle(StyleContext.DEFAULT_STYLE);
            
            mainText = sc.addStyle("MainText",defaultStyle);
            StyleConstants.setFontFamily(mainText,"arial");
            StyleConstants.setFontSize(mainText, 16);
            
            deco = sc.addStyle("deco",mainText);
            StyleConstants.setForeground(deco,Color.red);
            
            co = sc.addStyle("co", mainText);
            StyleConstants.setForeground(co, Color.green);
            
            srvMsg = sc.addStyle("srvMsg", mainText);
            StyleConstants.setForeground(srvMsg, Color.blue);
            StyleConstants.setAlignment(srvMsg,StyleConstants.ALIGN_CENTER);
            
            perso = sc.addStyle("perso",mainText);
            StyleConstants.setAlignment(perso,StyleConstants.ALIGN_RIGHT);
        }
    }

    public Style getMainText() {
        return mainText;
    }

    public void setMainText(Style mainText) {
        this.mainText = mainText;
    }

    public Style getDeco() {
        return deco;
    }

    public void setDeco(Style deco) {
        this.deco = deco;
    }

    public Style getCo() {
        return co;
    }

    public void setCo(Style co) {
        this.co = co;
    }

    public Style getPerso() {
        return perso;
    }

    public Style getSrvMsg() {
        return srvMsg;
    }
    
    
    
}
