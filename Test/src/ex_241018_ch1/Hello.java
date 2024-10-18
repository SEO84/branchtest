package ex_241018_ch1;

public class Hello {

	// 전역 메서드
	// public : 접근 지정자, 어디에서든 보인다, 또는 접근이 가능하다.
	// static : vs instance(클래스 객체를 생성한 개체)-> new 생성자(); 만들기.
	// static : 클래스 명으로 접근, 전역 자원으로 이용이됨.(공유자원)
	// int : 정수형, 기본형 타입(8가지), 참조형 타입(기본형을 제외하 나머지 전체 )
	// sum : 메서드(기능, 함수) 이름
	// (int a, int b) , 함수의 정의해서 사용하는 , 매개변수
	// 함수를 호출(사용), 함수가 정의가 된곳으로 이동함.
	// 리턴 타입 함수명(매개변수) -> int
	// return 키워드(예약어) 가 반드시 나와야하고, 반환 타입 결과, 정수형.
	public static int sum(int a, int b) {
		return a + b;
	}

	public static void main(String[] args) {
		// 위에서 정의한 함수 이용해보기.
		int result = sum(1, 2);

		System.out.println("클래스 메서드(static) 이용해보기 , result : " + result);
		
	}
	

}
