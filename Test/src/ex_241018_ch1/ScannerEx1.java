package ex_241018_ch1;

import java.util.Scanner;

public class ScannerEx1{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 입력 받기
        System.out.println("ID를 입력해주세요 : ");
        String userID = scanner.next();
        System.out.println("이름을 입력해주세요 : ");
        String userName = scanner.next();
        System.out.println("패스워드를 입력해주세요 : ");
        String userPassword = scanner.next();
        System.out.println("패스워드를 다시 입력해주세요 : ");
        String userPasswordconfirm = scanner.next();
        System.out.println("주소를 입력해주세요 : ");
        String userAddress = scanner.next();
        System.out.println("전화번호를 입력해주세요 : ");
        String userPhonenumber = scanner.next();
        System.out.println("좋아하는 메뉴를 입력해주세요 : ");
        String userFavoritemenu = scanner.next();

        // 출력 정렬
        System.out.println("\n======= " + userID + "의 회원 정보 =======");
        System.out.printf("%-15s | %s%n", "ID", userID);
        System.out.printf("%-15s | %s%n", "이름", userName);
        System.out.printf("%-15s | %s%n", "패스워드", userPassword);
        System.out.printf("%-15s | %s%n", "패스워드 확인", userPasswordconfirm);
        System.out.printf("%-15s | %s%n", "주소", userAddress);
        System.out.printf("%-15s | %s%n", "전화번호", userPhonenumber);
        System.out.printf("%-15s | %s%n", "좋아하는 메뉴", userFavoritemenu);
        System.out.println("=====================================");
    }
}

