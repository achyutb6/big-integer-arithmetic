package aab180004;

// Starter code for lp1.
// Version 1.0 (8:00 PM, Wed, Sep 5).

import java.util.Arrays;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.lang.Math;


public class Num  implements Comparable<Num> {

    static long defaultBase = 10;  // Change as needed
    long base = defaultBase;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    public Num(String s) {
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
    }
    public Num(){
        len = 0;
        arr = new long[100000];
    }

    public Num(long x) {
        this();
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
        Num out = new Num();
        long sum =0;
        long carry =0;
        int i=0;
        while(i<a.len && i<b.len)
        {
            sum = a.arr[i] + b.arr[i] + carry;
            out.arr[i] =sum % a.base;
            carry = sum /a.base;
            i++;
        }
        while(i<a.len)
        {
            sum = a.arr[i] + carry;
            out.arr[i] = sum % a.base;
            i++;
        }
        while(i<b.len)
        {
            sum = b.arr[i] + carry;
            out.arr[i] = sum % b.base;
            i++;
        }
        if(carry>0)
            out.arr[i] = carry;
        out.len =i;
        return out;
    }

    public static Num subtract(Num a, Num b) {
        long carry = 0;
        long sub =0;
        Num diff = new Num();
        Num x =a ,y =b;
        if(a.compareTo(b)<0)
        {
            x = b;
            y = a;
            diff.isNegative = true;
        }
        for(int i=0; i<y.len; i++)
        {
            sub = x.arr[i] - y.arr[i] - carry;
            if(sub < 0){
                sub += 10;
                carry += 1;
            }
            else{
                carry = 0;
            }
            diff.arr[i] = sub;
            diff.len++;
        }
        for(int j=y.len; j<x.len; j++)
        {
            sub = x.arr[j] - carry;
            if(sub < 0){
                sub += 10;
                carry += 1;
            }
            else{
                carry = 0;
            }
            if(sub !=0) {
                diff.arr[j] = sub;
                diff.len++;
            }
        }
        return diff;
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
        Num product = new Num();
        long carry;
        for(int i=0; i<b.len ; i++){
            carry=0;
            for(int j=0; j<a.len ; j++){
                product.arr[i+j] += carry + a.arr[j] * b.arr[i];
                carry = product.arr[i+j] / a.base;
                product.arr[i+j] = product.arr[i+j] % a.base;
                product.len++;
            }
            product.arr[i + a.len] = carry;
        }

        if(a.isNegative ^ b.isNegative)
            product.isNegative = true;
        else
            product.isNegative = false;

        return product;
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
        if( n == 0)
            return new Num(1);
        else if(n % 2 == 0 )
            return product(power(a,n/2),power (a,n/2));
        else
            return product(a,product(power(a,n/2),power (a,n/2)));
    }

    // Use binary search to calculate a/b
    public static Num divide(Num a, Num b) {
        return null;
    }

    // return a%b
    public static Num mod(Num a, Num b) {
        return null;
    }

    // Use binary search
    public static Num squareRoot(Num a) {
        return null;
    }


    // Utility functions
    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1 otherwise
    public int compareTo(Num other) {
        if(!this.isNegative && !other.isNegative)
            return compareLength(other);
        else if(this.isNegative && other.isNegative)
            return -1*compareLength(other);
        else if (this.isNegative && !other.isNegative)
            return -1;
        else
            return 1;
    }


    public int compareLength(Num other) {
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
    public String toString() {
        StringBuilder output = new StringBuilder();
        for(int i=len-1; i>=0; i--)
            output.append(arr[i]);
        return String.valueOf(output);
    }

    public long base() { return base; }

    // Return number equal to "this" number, in base=newBase
    public Num convertBase(int newBase) {
        int arrSize = 0;
        Num newNum = new Num("");
        arrSize = (int) Math.ceil((len+1)/Math.log10(base)+1);
        newNum.arr = new long[arrSize];
        return null;
    }

    // Divide by 2, for using in binary search
    public Num by2() {
        return null;
    }

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
        return null;
    }

    // Evaluate an expression in infix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluateInfix(String[] expr) {
        return null;
    }


    public static void main(String[] args) {
          Num s = new Num("50");
          Num t = new Num("10");

          Num u =Num.product(s,t);
          System.out.println((u.isNegative?"-":"")+u);

          Num v =Num.power(t,3);
          System.out.println((v.isNegative?"-":"")+v);


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
