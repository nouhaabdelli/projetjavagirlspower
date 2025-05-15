package gui;
import org.languagetool.JLanguageTool;
import org.languagetool.language.French;
import org.languagetool.rules.RuleMatch;

import java.util.List;

public class CorrectionServiceLocal {

    public String corrigerTexte(String texte) {
        try {
            JLanguageTool langTool = new JLanguageTool(new French());

            List<RuleMatch> matches = langTool.check(texte);
            StringBuilder texteCorrige = new StringBuilder(texte);

            // Appliquer les corrections en partant de la fin
            for (int i = matches.size() - 1; i >= 0; i--) {
                RuleMatch match = matches.get(i);
                List<String> suggestions = match.getSuggestedReplacements();
                if (!suggestions.isEmpty()) {
                    texteCorrige.replace(match.getFromPos(), match.getToPos(), suggestions.get(0));
                }
            }

            return texteCorrige.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return texte;
        }
    }
}
