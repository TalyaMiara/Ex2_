// Add your documentation below:

public class SCell implements Cell {
    private String line;
    private int type;
    private int order;

    public SCell(String s) {
        // Add your code here
        setData(s);// Set initial value for the cell
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
public void setData(String s) {
        this.line=s;
        // Update the type when setting the data

        if (isNumber(s)) {
            this.line=String.valueOf(Double.parseDouble(s));
            type = Ex2Utils.NUMBER;
        }  else if (s.startsWith("=")&&isForm(s)) {
            type = Ex2Utils.FORM;
        } else {
            type = Ex2Utils.TEXT; // invalid formula or input
        }
    }
    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        order = t;
    }
    public boolean isNumber(String text) {
        //על ידי try and catch, נרצה לבדוק אם הסטרינג שמוזן הוא מספר
        // הבדיקה פה היא בעצם האם אפשר לקחת ולהמיר ל-double או לא ולפי זה להכניס ל- try and catch
        if (text == null || text.isEmpty()) return false;
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public boolean isForm(String text) {
        //בדיקה ש-text הוא לא  null
        //בדיקה אם הוא לא empty
        //חייב להתחיל בשווה- return false
        //ואז בדיקה- בה קורה:
        //1-בדיקה שאין אופרטוק של כפל, חילוק, חיסוק וחיבור- עוקבים(לדוגמה ++ או --)
        //2- בדיקה על הסוגריים- שעל כל סוגר פותח יש סוגר שסוגר- שכמות הסוגריים הפותחות והסוגרות היא שווה
        //בבדיקה השלישית נרצה לבדוק מה קורה כאשר הסוגריים לא מסוגרים בסדר טוב:
        //  לכתוב, אז תכתוב לי קוד שיבצע בדיקה ע"י stringbuilder שיעשה את זהלכתו 3- בדיקה נוספת היא לבדוק שהסוגריים ממוקמים במקומות נכונים-לדוג(5+)3+5- ככה זה לא טוב

        if (text == null || text.isEmpty()) return false;

        //create new string from the 'text' string that start from the undex-1(without =)
        String content = text.substring(1);

        // בדיקה של תווים עוקבים לא תקינים (כמו ++ או --)
        if (content.matches(".*([+\\-*/])\\1{1,}.*")) {
            return false;
        }

        // בדיקה של סוגריים - התאמה בין סוגר פותח לסוגר סוגר
        int openCount = 0;
        for (char c : content.toCharArray()) {
            if (c == '(') openCount++;
            else if (c == ')') openCount--;
            if (openCount < 0) return false; // סוגר סוגר הופיע לפני סוגר פותח
        }
        if (openCount != 0) return false; // מספר לא שווה של סוגריים

        StringBuilder sb = new StringBuilder(content);

        for (int i = 0; i < sb.length() - 1; i++) {
            char current = sb.charAt(i);
            char next = sb.charAt(i + 1);

            // בדיקה: סוגר סוגר צמוד לסוגר פותח
            if (current == ')' && next == '(') {
                return false; // רצף לא חוקי
            }

            // בדיקה: סוגר סוגר ואחריו מספר תקין
            if (current == ')' && Character.isDigit(next)) {
                return false; // סוגר סוגר לא יכול להיות צמוד למספר
            }

            // בדיקה: מספר צמוד לסוגר פותח
            if (Character.isDigit(current) && next == '(') {
                return false; // מספר לא יכול לגעת בסוגר פותח
            }
        }

// בדיקה: סוגריים ריקים או סיום לא תקין
        if (content.endsWith("(") || content.startsWith(")")) {
            return false; // ביטוי לא יכול להסתיים בסוגר פותח או להתחיל בסוגר סוגר
        }

        return true;
    }

}
