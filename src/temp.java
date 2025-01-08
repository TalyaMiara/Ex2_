public class temp {
    public boolean isNumber(String text){
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

    public boolean isText(String text){
        //אם הוא לא פורמולה ולא נמבר- הוא טקסט.
        return text != null && !text.isEmpty() && !isForm(text) && !isNumber(text);

    }
    public boolean isForm(String text){
        //בדיקה ש-text הוא לא  null
        //בדיקה אם הוא לא empty
        //חייב להתחיל בשווה- return false
        //ואז בדיקה- בה קורה:
        //1-בדיקה שאין אופרטוק של כפל, חילוק, חיסוק וחיבור- עוקבים(לדוגמה ++ או --)
        //2- בדיקה על הסוגריים- שעל כל סוגר פותח יש סוגר שסוגר- שכמות הסוגריים הפותחות והסוגרות היא שווה
        //בבדיקה השלישית נרצה לבדוק מה קורה כאשר הסוגריים לא מסוגרים בסדר טוב:
        //  לכתוב, אז תכתוב לי קוד שיבצע בדיקה ע"י stringbuilder שיעשה את זהלכתו 3- בדיקה נוספת היא לבדוק שהסוגריים ממוקמים במקומות נכונים-לדוג(5+)3+5- ככה זה לא טוב

        if (text == null ) return false;
        if(text.isEmpty() ) return false;
        if( text.charAt(0) != '=')return false;
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

        // בדיקה של סדר סוגריים (לדוגמה: 5+)3 אינה תקינה)
        StringBuilder sb = new StringBuilder(content);
        for (int i = 0; i < sb.length() - 1; i++) {
            char current = sb.charAt(i);
            char next = sb.charAt(i + 1);
            if (current == ')' && (Character.isDigit(next) || next == '(')) return false;
        }

        return true;
    }
    public Double computeForm(String form){
        //בדיקה האם הטקסט הוא- isForm
        // כעת נרצה לבצע את החישוב ולהחזיר אותו כ- double
        // ניצור פונקציית עזר- שבה נבדוק- היא עוברת על הסטרינג ומחזיקה לנו כל פעם מה האופרטוק האחרון לפי הסדר שיבוצע
        // ומתוך הפונקציית עזר הזו- לפי האינדקס של האופרטור האחרון שיבוצע נחלק את הביטוי ל-2 substrings
        // הפעולה האחרונה שתיארתי כבר תתרחש בפונקציה זו-
        // ולאחר מכן זה יתבצע שוב ושוב באופן רקורסיבי- עד שנגיע למספר בודד
        //תנאי העצירה של הרקורסיה שלנו הם: הא המספר שהגעתי אליו הוא-isNumber- אם כן- תחזיר את המספר עצמו
        // ואז ברקורסיה זה יחזור אחורה ויחשב הכל..
        //ולבסוף יחזיר לנו את את החישוב הרצוי- שזוהי בעצם מטרת הפונקציה
        // לגבי הפונקציית עזר- תעבור על הסטריג-
        if (!isForm(form)) throw new IllegalArgumentException("Invalid formula format");
        String expression = form.substring(1);
        return evaluate(expression);
    }

    // פונקציה רקורסיבית להערכת ביטוי
    private Double evaluate(String expression) {
        expression = expression.trim();

        // תנאי עצירה: אם הביטוי הוא מספר
        if (isNumber(expression)) {
            return Double.parseDouble(expression);
        }

        // מציאת האופרטור הראשי האחרון שיבוצע
        int indexOfMainOperator = findMainOperator(expression);

        // פיצול הביטוי לשני חלקים
        String left = expression.substring(0, indexOfMainOperator).trim();
        String right = expression.substring(indexOfMainOperator + 1).trim();
        char operator = expression.charAt(indexOfMainOperator);

        // חישוב חלקי הביטוי וחיבורם לפי האופרטור
        Double leftValue = evaluate(left);
        Double rightValue = evaluate(right);

        switch (operator) {
            case '+': return leftValue + rightValue;
            case '-': return leftValue - rightValue;
            case '*': return leftValue * rightValue;
            case '/': return leftValue / rightValue;
            default: throw new IllegalArgumentException("Invalid operator");
        }
    }

    // פונקציה למציאת האופרטור הראשי האחרון שיבוצע
    private int findMainOperator(String expression) {
        int openParentheses = 0;
        int index = -1;
        int priority = -1;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == '(') {
                openParentheses++;
            } else if (c == ')') {
                openParentheses--;
            } else if (openParentheses == 0) {
                int currentPriority = operatorPriority(c);
                if (currentPriority > 0 && currentPriority >= priority) {
                    priority = currentPriority;
                    index = i;
                }
            }
        }
        return index;
    }

    // פונקציה לקבלת עדיפות של אופרטור
    private int operatorPriority(char operator) {
        switch (operator) {
            case '+':
            case '-': return 1;
            case '*':
            case '/': return 2;
            default: return -1;
        }
    }
}
