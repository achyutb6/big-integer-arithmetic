package aab180004;

// Starter code for lp1.
// Version 1.0 (8:00 PM, Wed, Sep 5).

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.lang.Math;


public class Num  implements Comparable<Num> {

    static long defaultBase = 10;  // Change as needed
    long base = 1000000000;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    public Num(String s) {
        this.base = defaultBase;
        len = s.length();
        int i=0,j;
        if(s.charAt(0) == '-'){
            isNegative = true;
            i++;
            len--;
        }
        else{
            isNegative = false;
        }
        arr = new long[len];
        for(j=len-1;j>=0;j--,i++){
            arr[j] = Character.digit(s.charAt(i),10);
        }

        arr = convertBase(arr,10,1000000000);
        this.base = 1000000000;
        this.len = arr.length;
    }
    public Num(){
        len = 0;
        arr = new long[1000];
    }

    public Num(long arr[]){
        this(arr,1000000000);
    }
    public Num(long arr[], long base){
        this.arr = arr;
        this.len = arr.length;
        this.base = base;
    }

    public Num(long x) {
        this(x,1000000000);
    }
    public Num(long x, long base) {
        this();
        this.base = base;
        int i = 0;
        if(x==0){
            arr[i] = 0;
            i++;
        }
        if(x<0){
            isNegative = true;
            x *=-1;
        }
        while(x>0)
        {
            isNegative = false;
            arr[i] = x % base;
            x = x / base;
            i++;
        }
        len = i;
    }

    public static Num add(Num a, Num b) {
        if(a.base != b.base)
            throw new NumberFormatException();
        long[] out = new long[Math.max(a.len,b.len)+1];
        long sum = 0;
        long carry = 0;
        int i=0;
        while(i<a.len && i<b.len)
        {
            sum = a.arr[i] + b.arr[i] + carry;
            out[i] =sum % a.base;
            carry = sum /a.base;
            i++;
        }
        while(i<a.len)
        {
            sum = a.arr[i] + carry;
            out[i] = sum % a.base;
            carry = sum / a.base;
            i++;
        }
        while(i<b.len)
        {
            sum = b.arr[i] + carry;
            out[i] = sum % b.base;
            carry = sum / a.base;
            i++;
        }
        if(carry>0)
            out[i] = carry;

        return new Num(out[out.length-1]==0?Arrays.copyOfRange(out,0,out.length-1):out,a.base);
    }

    public static Num subtract(Num a, Num b) {
        if(a.base != b.base)
            throw new NumberFormatException();
        long carry = 0;
        Num zero = new Num(0);
        long[] diff = new long[Math.max(a.len,b.len)];
        Num x =a ,y =b;
        if(a.compareTo(b)<0)
        {
            x = b;
            y = a;
        }
        if(x.compareTo(zero) ==0)
            return y;
        if(y.compareTo(zero)==0)
            return x;

        for(int i=0; i<y.len; i++)
        {
            long sub = x.arr[i] - y.arr[i] - carry;
            if(sub < 0){
                sub += x.base; //change base
                carry = 1;
            }
            else{
                carry = 0;
            }
            diff[i] = sub;
        }
        for(int j=y.len; j<x.len; j++)
        {
            long sub = x.arr[j] - carry;
            if(sub < 0){
                sub += x.base; //change base
                carry = 1;
            }
            else{
                carry = 0;
            }
//            if(sub !=0) {
            diff[j] = sub;
//            }
        }
        int k = diff.length-1;
        while(k>=0 && diff[k] == 0)
            k--;
        if(k == -1)
            return new Num(0,a.base);
        if(k == 0)
            return  new Num(diff[0],a.base);

        Num output = new Num(Arrays.copyOfRange(diff,0,k+1),a.base);
        if(a.compareTo(b)<0)
        {
            output.isNegative = true;
        }
        return output;
    }

    /*public static Num subtract(Num a, Num b) {
        Num diff = new Num();
        Num x =a ,y =b;
        if(a.compareTo(b)<0)
        {
            x = b;
            y= a;
            diff.isNegative = true;
        }
        for(int i=0 ; i<y.len ;i++){
            y.arr[i] = 9 - y.arr[i];
        }
        diff = add(x,y);

        return diff;
    }*/

    public static Num product(Num a, Num b) {
        if(a.base != b.base)
            throw new NumberFormatException();
        long[] product = new long[a.len+b.len];
        Num zero = new Num(0,a.base);
        if(a.compareTo(zero)==0 || b.compareTo(zero)==0)
            return zero;
        long carry;
        for(int i=0; i<b.len ; i++){
            carry=0;
            for(int j=0; j<a.len ; j++){
                product[i+j] += carry + a.arr[j] * b.arr[i];
                carry = product[i+j] / a.base;
                product[i+j] = product[i+j] % a.base;
            }
            product[i + a.len] = carry;
        }
        Num output;
        if(product[product.length-1]==0)
            output = new Num(Arrays.copyOfRange(product,0,product.length-1),a.base);
        else
            output = new Num(product,a.base);

        if(a.isNegative ^ b.isNegative)
            output.isNegative = true;
        else
            output.isNegative = false;

        return output;
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
        if( n == 0)
            return new Num(1,a.base);
        else if(n % 2 == 0 )
            return product(power(a,n/2),power (a,n/2));
        else
            return product(a,product(power(a,n/2),power (a,n/2)));
    }

    // Use binary search to calculate a/b
    public static Num divide(Num a, Num b) {
        if(a.base != b.base)
            throw new NumberFormatException();
        Num left = new Num(0,a.base);
        Num right = new Num (Arrays.copyOf(a.arr,a.len),a.base);
        Num prevMid = new Num (0,a.base);

        Num temp1,temp2;

        while(true){
            temp1 = subtract(right,left);
            temp2 = temp1.by2();
            Num mid = add(left,temp2);
            if(product(b,mid).compareTo(a) == 0 || prevMid.compareTo(mid)==0)
                return mid;
            if(product(b,mid).compareTo(a) < 0)
                left = mid;
            else
                right = mid;
            prevMid =mid;
        }
    }

    // return a%b
    public static Num mod(Num a, Num b) {
        if(a.base != b.base)
            throw new NumberFormatException();
        Num ZERO = new Num(0,a.base);
        Num ONE = new Num(1,a.base);
        if(b.compareTo(ZERO) ==0)
            return null;
        if(a.compareTo(b) == 0 )
            return ZERO;
        if(b.compareTo(ONE) == 0)
            return ZERO;

        Num temp1 = divide(a,b);
        Num temp2 = product(b,temp1);
        Num temp3 = subtract(a,temp2);

        return (temp3);
    }

    // Use binary search
    public static Num squareRoot(Num a) {
        Num ZERO = new Num(0,a.base);
        Num ONE = new Num(1,a.base);
        if(a.compareTo(ZERO)==0 || a.compareTo(ONE)==0)
            return a;

        Num start = ONE;
        Num end = a;
        Num result = ZERO;
        Num prevMid = ZERO;

        while(start.compareTo(end) <=0){
            Num mid = (add(start,end)).by2();

            Num prod = product(mid,mid);
            if(prod.compareTo(a)== 0 || prevMid.compareTo(mid) == 0 )
                return  mid;

            if(prod.compareTo(a)<0){
                start = add(mid,ONE);
                result = mid;
            }
            else{
                end = mid;
            }
            prevMid = mid;
        }
        return result;
    }


    // Utility functions
    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1 otherwise
    public int compareTo(Num other) {
        if(!this.isNegative && !other.isNegative)
            return unsignedCompareTo(other);
        else if(this.isNegative && other.isNegative)
            return -1*unsignedCompareTo(other);
        else if (this.isNegative && !other.isNegative)
            return -1;
        else
            return 1;
    }


    public int unsignedCompareTo(Num other) {
        if (this.len<other.len) {
            return -1;
        } else if (this.len>other.len) {
            return +1;
        } else {
            return compareMagnitude(other);
        }
    }


    public int compareMagnitude(Num other) {
        for (int i = this.len - 1; i >= 0; i--) {
            if (this.arr[i] < other.arr[i]) {
                return -1;
            } else if (this.arr[i] > other.arr[i]) {
                return 1;
            }
        }
        return 0;
    }

    // Output using the format "base: elements of list ..."
    // For example, if base=100, and the number stored corresponds to 10965,
    // then the output is "100: 65 9 1"
    public void printList() {
        StringBuilder output = new StringBuilder();
        output.append(base+":");
        if(isNegative)
            output.append(" -");
        for(int i =0; i<len; i++){
            output.append(" "+arr[i]);
        }
        System.out.print(output);
    }

    // Return number to a string in base 10
//    public String toString() {
//        StringBuilder output = new StringBuilder();
//        Num base10 = this.convertBaseTo10();
//        for(int i=base10.len-1; i>=0; i--)
//            output.append(base10.arr[i]);
//        return String.valueOf(output);
//    }

    public long base() { return base; }

    // Return number equal to "this" number, in base=newBase
    public Num convertBase(int newBase) {
        Num thisNum = new Num(this.arr);
        long[] newNum = convertBase(thisNum.arr,(int)thisNum.base,newBase);
        Num result = new Num(newNum,newBase);
        return result;
    }

    public String toStringWithoutBaseChange() {
        StringBuilder output = new StringBuilder();
        for(int i=len-1; i>=0; i--)
            output.append(arr[i]);
        return String.valueOf(output);
    }
    public String toString() {
        StringBuilder output = new StringBuilder();
        for(int i=len-1; i>=0; i--)
            output.append(arr[i]);
        return String.valueOf(output);
    }

    public long[] convertBase(long[] thisNumArr, int currentBase,int newBase){
        Num ZERO = new Num(0,currentBase);
        int arrSize = 0;
        Num thisNum = new Num(thisNumArr,currentBase);
        Num b = new Num(newBase,currentBase);
        arrSize = (int) Math.ceil((thisNum.len+1)/Math.log10(newBase)+1);
        long[] newNum = new long[1000];
        int i =0;
        while(thisNum.compareTo(ZERO) > 0){
            newNum[i] = Long.parseLong(mod(thisNum,b).toStringWithoutBaseChange());
            thisNum = divide(thisNum,b);
            i++;
        }
        int k = arrSize -1;
        while(k>=0 && newNum[k] == 0)
            k--;
        if(k == -1)
            return new long[]{0};
        if(k == 0)
            return new long[]{newNum[0]};

        return Arrays.copyOfRange(newNum,0,k+1);
    }
    public Num convertBaseTo10() {
        Num ZERO = new Num(0,10);
        Num thisNum = new Num(this.arr,10);
        int arrSize = 0;
        arrSize = (int) Math.ceil((len+1)/Math.log10(10)+1);
        Num newNum = ZERO;
        for(int i=0 ; i< thisNum.len ; i++){
            long temp =(long)(thisNum.arr[i] * Math.pow(base, i));
            newNum = add(newNum,new Num(temp,10));
        }
        return newNum;
    }

/*    public Num convertBase(int newBase) {
        Num ZERO = new Num(0);
        Num thisNum = new Num(this.arr);
        Num b = new Num(Integer.toString(newBase));
        int arrSize = 0;
        arrSize = (int) Math.ceil((len+1)/Math.log10(base)+1);
        Num newNum = ZERO;
        for(int i=0 ; i< len ; i++){
            long temp =(long)(thisNum.arr[i] * Math.pow(newBase, i));
            newNum = add(newNum,new Num(temp));
        }
        return newNum;
    }*/


    // Divide by 2, for using in binary search
    public Num by2() {
        Num ZERO = new Num(0,this.base);
        Num one = new Num(1,this.base);
        if(this.compareTo(one)==0 || this.compareTo(ZERO) == 0)
            return ZERO;
        long[] output = Arrays.copyOf(this.arr,this.len);
        long carry = 0;
        for(int i=len-1; i>=0 ; i-- ){
            output[i] = output[i] + carry;
            if(output[i] % 2 == 1)
                carry = this.base; //change base
            else
                carry = 0;
            output[i] = output[i] / 2;
        }
        Num by2;
        if(output[output.length-1]==0) {
            by2 = new Num(Arrays.copyOfRange(output, 0, output.length - 1),this.base);
        }
        else
            by2 = new Num(output,this.base);

        //return new Num(output[output.length-1]==0?Arrays.copyOfRange(output,0,output.length-1):output);
        return  by2;
    }

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
        ArrayDeque<Num> stack = new ArrayDeque<>();

        for(int i=0 ; i<expr.length ; i++){
            if(Character.isDigit(expr[i].charAt(0))){
                stack.push(new Num(expr[i]));
            }
            else{
                Num val1 = stack.pop();
                Num val2 = stack.pop();

                switch(expr[i]){
                    case "+":
                        stack.push(add(val2,val1));
                        break;

                    case "-":
                        stack.push(subtract(val2,val1));
                        break;

                    case "/":
                        stack.push(divide(val2,val1));
                        break;

                    case "*":
                        stack.push(product(val2,val1));
                        break;

                    case "%":
                        stack.push(mod(val2,val1));
                        break;

                    case "^":
                        stack.push(power(val2,Long.parseLong(val1.toString())));
                        break;

                }

            }

        }

        return stack.pop();
    }

    // Evaluate an expression in infix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluateInfix(String[] expr) {

        ArrayDeque<Num> values = new ArrayDeque<>();
        ArrayDeque<String> operators = new ArrayDeque<>();

        for(int i=0 ; i<expr.length; i++){
            if(Character.isDigit(expr[i].charAt(0))){
                values.push(new Num(expr[i]));
            }
            else if(expr[i].equals("(")){
                operators.push(expr[i]);
            }
            else if(expr[i].equals(")")){
                while(!operators.peek().equals("(")){
                    values.push(evaluate(operators.pop(),values.pop(),values.pop()));
                }
                operators.pop();
            }else{
                while(!operators.isEmpty() && hasPrecedence(expr[i],operators.peek())){
                    values.push(evaluate(operators.pop(),values.pop(),values.pop()));
                }
                operators.push(expr[i]);
            }
        }

        while (!operators.isEmpty()){
            values.push(evaluate(operators.pop(),values.pop(),values.pop()));
        }

        return values.pop();
    }

    public static boolean hasPrecedence(String operator1, String operator2){
        if(operator2.equals("(") || operator2.equals(")")){
            return false;
        }
        if((operator1.equals("*") || operator1.equals("/")) && (operator2.equals("+") || operator2.equals("-"))){
            return false;
        }else{
            return true;
        }
    }

    public static Num evaluate(String operator,Num a,Num b){
        switch(operator){
            case "+":
                return add(b,a);

            case "-":
                return (subtract(b,a));

            case "/":
                return  (divide(b,a));

            case "*":
                return  (product(b,a));

            case "%":
                return (mod(b,a));

            case "^":
                return  (power(b,Long.parseLong(a.toString())));

        }
        return new Num(0);
    }

    public static void main(String[] args) {
        Num s = new Num("100");
        Num t = new Num(100);

        String[] input = {"10","+","2","*","6"};

        Num p = evaluateInfix(input);
        System.out.println((p.isNegative?"-":"")+p);

        //BigInteger number = new BigInteger("214748364888888888888888888888",10);
        //System.out.println(number.toString(555));

        System.out.println(s);
        s.printList();
//            for(int i=0;i<10;i++)
//                System.out.println(s=s.by2());
//          Num p = Num.add(s,t);
//          System.out.println((p.isNegative?"-":"")+p);
//
//          Num u =Num.product(s,t);
//          System.out.println((u.isNegative?"-":"")+u);
//
//          Num v =Num.power(s,3);
//          System.out.println((v.isNegative?"-":"")+v);
//
//          Num w = Num.squareRoot(s);
//          System.out.println((w.isNegative?"-":"")+w);


        //System.out.println(s.compareTo(t));
        //Num x = new Num("-5236");
        //Num y = new Num("5236");

        //Num z = Num.add(x, y);
//        System.out.println(z);
//        Num a = Num.power(x, 8);
//        System.out.println(a);
        // if(x != null) x.printList();
    }


}
