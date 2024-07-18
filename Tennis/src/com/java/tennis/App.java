package com.java.tennis;

import java.util.Scanner;

import com.java.tennis.service.ExplainService;
import com.java.tennis.service.TennisService;
import com.java.tennis.view.MenuView;
import com.java.tennis.view.RecordView;

public class App {
	public static int langIndex = 0;
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		MenuView view = new MenuView();
		ExplainService explainService = new ExplainService();
		TennisService tennisService = new TennisService();
//		LanguageService languageService = new LanguageService();
		
		RecordView rView =  new RecordView();
		
		boolean yn= true;
		while(yn) {
			view.getMainMenu();
			
			String input = scan.nextLine();
			
			if (input.equals("1")) {
//				게임시작
				tennisService.gameSetup();
			} else if (input.equals("2")) {
				rView.mainMenu();
				yn = false;
//				명예의 전당
			} else if (input.equals("3")) {
//				게임 설명서
				explainService.get();
			} else if (input.equals("4")) {
//				환경설정
			} else if (input.equals("5")) {
				System.out.println("게임을 종료합니다.");
				System.out.println("안녕히 가십시오.");
				break;
			} else {
				System.out.println("잘못 입력 하셨습니다.");
				System.out.println("다시 입력해주세요.");
			}
		}
	}
}