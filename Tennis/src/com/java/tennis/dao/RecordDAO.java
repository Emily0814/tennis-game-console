package com.java.tennis.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Stack;

import com.java.tennis.model.RecordDTO;
import com.java.tennis.view.MainView;
import com.java.tennis.view.RecordView;

public class RecordDAO {
	private RecordView view;
	private Scanner scan;
	private Stack<String> historyStack;
	private String currentPage;

	public RecordDAO() {
		this.view = new RecordView();
		this.scan = new Scanner(System.in);
	}

	private final String PATH = ".\\resource\\"; // 변경되면 안됌

	// 정보 불러오는 메서드----------------------------------------------------------------
	// 간단한 정보 불러오기
	public void get() {

		String line = null;
		int count = 0;
		String[] temp = null;
		int num = 1;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(PATH + "record.txt"));

			// 1,2024-04-03,현영석,3,2,0
			// 번호 날짜 닉네임 캐릭터 스코어 점수

			while ((line = reader.readLine()) != null) {

				temp = line.split(",");

				String character = nameCharacter(temp[3]);

				String score = "";
				score = temp[4] + " : " + temp[5];

				String list = String.format("\t%2s\t\t%s\t%s\t\t%s\t\t%s", temp[0], temp[1], temp[2], character, score);

				System.out.println(list);
				count++;
				if (count == 10) { // 10줄만 보이고 싶어
					break;
				}

			}

			view.dividingLine(); // 구분선
			System.out.println();

			reader.close();
		} catch (Exception e) {
			System.out.println("RecordDAO.get");
			e.printStackTrace();
		}

	}

	// 상세한 정보 불러오기
	public void getSpec(String num) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(PATH + "game.txt"));

			String line = null;

			while ((line = reader.readLine()) != null) {

				if (line.startsWith(num)) {

					String[] temp = line.split(",");

					// 1,1,1,4,0
					// 번호 세트 게임 스코어점수
					//

					String score = "";
					score = temp[3] + " : " + temp[4];

					String win = "";
					if ((temp[3].compareTo(temp[4])) > 0) {
						win = "승";
					} else {
						win = "패";
					}

					String list = String.format("\t%s세트\t\t%s게임\t\t%s\t\t %s", temp[1], temp[2], score, win);

					System.out.println(list);

				}

			}
		} catch (Exception e) {
			System.out.println("RecordDAO.getSpec");
			e.printStackTrace();
		}

	}

	// 검색 기능 : 이름 검색하면 불러올 수 있는 기능
	public String recordSearch(String id) {

		// 간단한 점수 저장------------------------------------------
		// 일련번호, 날짜, 이름, 캐릭터 번호, 스코어1(나), 스코어2(컴퓨터)
		// 1,2024-04-03,현영석,3,2,0
		String result = "";
		String line = null;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(PATH + "record.txt"));

			while ((line = reader.readLine()) != null) {
				String[] temp = line.split(",");

				if (temp[2].equals(id)) {
					String character = nameCharacter(temp[3]);

					String score = "";
					score = temp[4] + " : " + temp[5];

					String list = String.format("\t%2s\t\t%s\t%s\t\t%s\t\t%s\n", temp[0], temp[1], temp[2], character,
							score);

					result += list;
//					System.out.println(result);
				}
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("RecordDAO.recordSearch");
			e.printStackTrace();
		}
		return result;
	}

	public String gameId() {

		System.out.print("아이디 입력 : ");
		String id = scan.nextLine();

		return id;
	}

	public String gameNum() {

		System.out.print("번호 입력 : ");
		String num = scan.nextLine();

		return num;

	}

	public void getTotal() {

		String line = null;

		String[] temp = null;
		int num = 1;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(PATH + "record.txt"));

			// 1,2024-04-03,현영석,3,2,0
			// 번호 날짜 닉네임 캐릭터 스코어 점수
			//
			while ((line = reader.readLine()) != null) {

				temp = line.split(",");

				String character = nameCharacter(temp[3]);

				String score = "";
				score = temp[4] + " : " + temp[5];

				String list = String.format("\t%2s\t\t%s\t%s\t\t%s\t\t%s", num, temp[1], temp[2], character, score);

				System.out.println(list);

				num++;

			}
			view.dividingLine(); // 구분선
			System.out.println();

		} catch (Exception e) {
			System.out.println("RecordDAO.get");
			e.printStackTrace();
		}

	}

	public void sort(String number) {
		String line = null;
		String lineTemp = null;
		String[] temp = null;
		int num = 1;

		try {
			// 일련번호, 날짜, 이름, 캐릭터 번호, 스코어1(나), 스코어2(컴퓨터)
			// 1,2024-04-03,현영석,3,2,0
			ArrayList<RecordDTO> list = new ArrayList<RecordDTO>();
			BufferedReader reader = new BufferedReader(new FileReader(PATH + "record.txt"));
			while ((line = reader.readLine()) != null) {

				temp = line.split(",");
				temp[1] = temp[1].replace("-", "");
				RecordDTO dto = new RecordDTO();
				dto.setNo(temp[0]); // 일련번호
				dto.setDate(temp[1]); // 날짜
				dto.setName(temp[2]); // 이름
				dto.setCharacterno(temp[3]); // 캐릭터 > 번호를 이름으로 변경하는 메서드 만들어야 함

				dto.setCharactername(temp[3]); // 캐릭터이름
				dto.setScoreme(temp[4]); // 내 점수
				dto.setSocrecumputer(temp[5]); // 컴퓨터 점수

				dto.setCharactername(nameCharacter(temp[3]));

				list.add(dto);

//					

			}

			stack(list, number); // 정렬 메서드

			int i = 1;
			for (RecordDTO record : list) {

				String dump = record.getDate();
				dump = dump.substring(0, 4) + "-" + dump.substring(4, 6) + "-" + dump.substring(6);

				String character = nameCharacter(record.getCharacterno());
//			

				String tempRecord = "";
				tempRecord = String.format("%d\t%s\t%s\t%s\t%s : %S", i, dump, record.getName(),
						record.getCharactername(), record.getScoreme(), record.getSocrecumputer());
				System.out.println(tempRecord);

				i++;
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stack(ArrayList<RecordDTO> list, String number) {

		view.sortMenu();
		String sort = scan.nextLine();

		if (sort.equals("1") || sort.equals("2")) {
			if (number.equals("1")) {// 날짜
				if (sort.equals("1")) {
					list.sort(Comparator.comparing(RecordDTO::getDate)); // 정렬(오름차순)

				} else {
					list.sort(Comparator.comparing(RecordDTO::getDate).reversed()); // 정렬(내림차순)
				}
			} else if (number.equals("2")) {
				if (sort.equals("1")) {
					list.sort(Comparator.comparing(RecordDTO::getName)); // 정렬(오름차순)

				} else {
					list.sort(Comparator.comparing(RecordDTO::getName).reversed()); // 정렬(내림차순)
				}
			} else if (number.equals("3")) {
				if (sort.equals("1")) {
					list.sort(Comparator.comparing(RecordDTO::getCharactername)); // 정렬(오름차순)

				} else {
					list.sort(Comparator.comparing(RecordDTO::getCharactername).reversed()); // 정렬(내림차순)
				}
			}
		} else {
			MainView error = new MainView();
			error.errorInput();
		}

	}

	public static String nameCharacter(String number) {

		String character = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(".\\resource\\character.txt"));
			String[] tempCharacter = new String[4];
			String[] tempRecorde = new String[4];
			String line = null;
			int i = 0;

			while ((line = reader.readLine()) != null) {

				if (i > 0) { // 첫 번째 줄은 무시
					tempCharacter = line.split(",");
					if (tempCharacter.length > 1) {
						tempRecorde[i - 1] = tempCharacter[1].trim(); // 이름만 추출해서 저장
					}
				}

				i++;
			}

			if (number.equals("1")) {
				character = tempRecorde[Integer.parseInt(number) - 1];
			} else if (number.equals("2")) {
				character = tempRecorde[Integer.parseInt(number) - 1];
			} else if (number.equals("3")) {
				character = tempRecorde[Integer.parseInt(number) - 1];
			} else if (number.equals("4")) {
				character = tempRecorde[Integer.parseInt(number) - 1];
			}

			reader.close();

		} catch (Exception e) {
			System.out.println("에러");
		}

		return character;
	}

}
