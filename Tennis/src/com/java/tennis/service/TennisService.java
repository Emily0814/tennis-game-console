package com.java.tennis.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

import com.java.tennis.App;
import com.java.tennis.dao.CharacterDAO;
import com.java.tennis.dao.GameDAO;
import com.java.tennis.dao.MatchDAO;
import com.java.tennis.model.AbilityDTO;
import com.java.tennis.model.CharacterDTO;
import com.java.tennis.model.MatchDTO;
import com.java.tennis.model.RecordDTO;
import com.java.tennis.model.TennisDTO;
import com.java.tennis.view.GameView;
import com.java.tennis.view.MainView;
import com.java.tennis.view.SettingView;
import com.java.tennis.view.TennisView;

public class TennisService {
	
	final String PATH = "resource\\record.txt";

	final String PATHCharacter = "resource\\character.txt";
	
	private TennisView view = new TennisView();
//	private TennisDAO dao = new TennisDAO();
	private TennisDTO dto = new TennisDTO();
	private Scanner scan = new Scanner(System.in);
	
	static int countTotalServe; //한 매치에 총 서브 횟수 (매치가 끝날 때 리셋)
	static int countServe; //몇 번째 서브 (게임이 끝날 때 리셋)
	static int countGame; //몇 번째 게임 (세트가 끝날 때 리셋)
	static int countSet; //몇 번째 세트 (매치가 끝날 때 리셋)
	
	private GameDAO dao = new GameDAO();
	private TennisDTO me = new TennisDTO();
	private TennisDTO cpu = new TennisDTO();
	private RecordDTO dtoRecord = new RecordDTO();
	private CharacterDAO daoCharacter = new CharacterDAO();
	private SettingView settingView = new SettingView();
	private MainView mainView = new MainView();
	private MatchDTO dtoMatch = new MatchDTO();
	private MatchDAO daoMatch = new MatchDAO();
	private String temp = "";
	
//	private int point;		//포인트
//	private int gamePoint; //게임 포인트
//	private int setPoint; //세트 포인트
//	
//	static int serveCount; //몇 번째 서브 (게임이 끝날 때 리셋)
//	static int gameCount; //몇 번째 게임 (세트가 끝날 때 리셋)
//	static int setCount; //몇 번째 세트 (매치가 끝날 때 리셋)

	public TennisService() {
		this.dto = new TennisDTO();
		this.view = new TennisView();
//		this.dao = new TennisDAO();
		this.scan = new Scanner(System.in);
		this.settingView = new SettingView();
		this.mainView = new MainView();
	}

	public void gameSetup() {
//		단식/복식 -> 세트 수 -> 플레이어수 -> 캐릭터 선택
		
		int type = 0; // 1 = 단식, 2 = 복식
		int set = 0; // 1 = 3세트, 2 = 5세트
		int player = 0; //1 = 유저 1명, 2 = 유저 2명
		int character = 0; //게임 캐릭터 고유일련번호
		
		boolean loop = true;
		while (loop) {
			view.selectGameType();
			type = scan.nextInt();
			dto.setType(type);
			scan.skip("\r\n");
			
			if (type == 1 || type == 2) {
				loop = false;
			} else {
				mainView.errorInput();
				continue;
			}
		}
		
		loop = true;
		while (loop) {
			view.selectGameSet();
			set = scan.nextInt();
			dto.setSet(set);
			scan.skip("\r\n");
			
			if (set == 1 || set == 2) {
				loop = false;
			} else {
				mainView.errorInput();
				continue;
			}
		}
		
		loop = true;
		while (loop) {
			
			view.selectPlayerNumber();
			player = scan.nextInt();
			dto.setPlayer(player);
			scan.skip("\r\n");
			
			if (player == 1 || player == 2) {
				loop = false;
			} else {
				mainView.errorInput();
				continue;
			}
		}
		
		loop = true;
		while (loop) {
			
			System.out.println();
			view.characterSelect();
			character = scan.nextInt();
			scan.skip("\r\n");
			
			if (character == 1 || character == 2|| character == 3 || character ==4) {
				loop = false;
			} else {
				mainView.errorInput();
				continue;
			}
		}
		
		
		;
		dto.setType(type);
		dto.setSet(set);
		dto.setPlayer(player);

		
		gameStart(dto, daoCharacter.get(character));
	
	}

	private void gameStart(TennisDTO dto, CharacterDTO dtoCharacter) {
		
		String p1 = "Player1";
		String p2 = "Player2";
		
		countServe = 1;
		countTotalServe = 1;
		countGame = 1;
		countSet = 1;
		
//		me.pointSet = 1;	//게임 빨리 끝내기용
//		me.pointGame = 2;	//게임 빨리 끝내기용
//		countSet = 2;		//게임 빨리 끝내기용
//		countGame = 3;		//게임 빨리 끝내기용
		
		view.getTitle();
		
//		int matchId = 0;
//		matchId = matchId();
		
		boolean loop = true;
		while (loop) {
			
			pointCheck(dtoCharacter);
			
			if (dto.getSet() == 1) {
				if (me.pointSet == 2 || cpu.pointSet == 2) {
					break;
				}
			} else if (dto.getSet() == 2) {
				if (me.pointSet == 3 || cpu.pointSet == 3) {
					break;
				}
			}

			view.informGame(countSet, countGame, countTotalServe);

			AbilityDTO dtoAbility = new AbilityDTO();
			int input;
			GameView viewGame = new GameView();
			while (true) {
				
				viewGame.gameView(dtoCharacter);
				input = scan.nextInt();
				scan.skip("\r\n");
				
				if (input == 333) {
					
					String[] terms = {"유저 포인트", "컴퓨터 포인트", "유저 게임 포인트", "컴퓨터 게임 포인트", "유저 세트 포인트", "컴퓨터 세트 포인트", "현재 게임", "현제 세트"};
					String txt = "" + me.point + "," + cpu.point + "," + me.pointGame + "," + cpu.pointGame + "," + me.pointSet + "," + cpu.pointSet + "," + countGame + "," + countSet;
					String[] old = txt.split(",");
					int[] values = {me.point, cpu.point, me.pointGame, cpu.pointGame, me.pointSet, cpu.pointSet, countGame, countSet};
					
//					int[] temp = scoreModifier(me.point, cpu.point, me.pointGame, cpu.pointGame, me.pointSet, cpu.pointSet, countGame, countSet);
					scoreModifier(values);
					
					me.point = temp[0];
					cpu.point = temp[1];
					me.pointGame = temp[2];
					cpu.pointGame = temp[3];
					me.pointSet = temp[4];
					cpu.pointSet = temp[5];
					countGame = temp[6];
					countGame = temp[7];
					
					System.out.printf("게임 변수가 아래와 같이 변경되었습니다.\r\n");
					
					for (int i=0; i<terms.length; i++) {
						System.out.printf("%s: %s -> %d\r\n", terms[i], old[i], temp[i]);
					}
					
					System.out.println();
					continue;
				}

				if (input == 111 || input == 222) {
					break;
				} else {
				if (input > 4 || input < 1) {
					view.selectSkill();
					continue;
				} else {
					break;
				}
			}	
				
			}
			
	/*
			사용자가 원하는 기술에 해당하는 번호 input을 입력할 때 사용하기 위해 추가한 코드
			특정 점수/상황을 도출하기 위한 용도로 사용 (ex: 같은 점수 상황을 만들기. 타이 브레이크 상황 만들기 등...)
			추가적인 코드는 statModifier(input) 메서드 참조 (해당 메서드는 AbilityDTO 클래스에 존재함)
			111 입력 시 -> 유저 난수 += 150 -> 유저 무조건 승리
			222 입력 시 -> 컴퓨너 난수 += 150 -> 컴퓨터 무조건 승리 
			if (input == 111 || input == 222) {
				break;
			} else {}
	*/
			
			
			
			Random rnd = new Random();
			me.chance = rnd.nextInt(100) + 1 + dtoAbility.statModifier(input); //stats[i]
			cpu.chance = rnd.nextInt(100) + 1;

			if(App.difficulty == settingView.EASY) {
				me.chance += 20;
			} else if(App.difficulty == settingView.HARD) {
				me.chance -= 20;
			}
	
			
			if (me.chance > cpu.chance) {
				me.point++;
				countTotalServe++;
				countServe++;
//				System.out.println(p1 + " 득점!"); //System.out.println(pointName(me.point, cpu.point)); 메서드가 득점 출력 메시지 대체
				
//				여기에 본인/컴퓨터 포인트를 인자값으로 받아서 러브 피프틴 써티 포티 이런 걸 말해주는 메서드 구현 필요
			} else if (me.chance < cpu.chance) {
				cpu.point++;
				countTotalServe++;
				countServe++;
//				System.out.println(p2 + " 득점!"); //System.out.println(pointName(me.point, cpu.point)); 메서드가 득점 출력 메시지 대체	
				
//				여기에 본인/컴퓨터 포인트를 인자값으로 받아서 러브 피프틴 써티 포티 이런 걸 말해주는 메서드 구현 필요
			} else {
				continue;
			}
			System.out.println(pointName(me.point, cpu.point));
			System.out.println(me.point + "-" + cpu.point);
			System.out.println("[확인]");
			scan.nextLine();
			System.out.println();
			
			
		}
		boolean loopRecord = true;
		while (loopRecord) {
			view.recordGame();
			int input = scan.nextInt();
			scan.skip("\r\n");
			
			if (input == 1) {
				recordName(dtoCharacter);
				loopRecord = false;
				
			} else if (input == 2) {
				break;
			}
		}
		
	}

//	private int[] scoreModifier(int p1, int p2, int g1, int g2, int s1, int s2,	int countGame, int countSet) {
	private int[] scoreModifier(int[] values) {
		
		/*
		해당 메서드는 게임내 포인트,게임,세트 점수를 조작하여 원하는 디버깅 상황을 신속하게 만들기 위해 만든 메서드입니다.
		해당 메서드는 게임 내 각종 점수 변수를 정수형의 배열로 받고 다시 이것을 같은 자료형의 새 값으로 반환합니다.
		[1] = 유저 포인트,	[2] = 컴퓨터 포인트
		[3] = 유저 게임 포인트,	[4] = 유저 게임 포인트
		[5] = 유저 세트 포인트, [6] = 유저 세트 포인트
		countGame = 현재 게임 == [3] + [4] + 1
			ex: 유저가 1 게임 포인트를 획득했으며 컴퓨터가 0 게임 포인트를 획득하고 있으면 식은 아래와 같습니다.
			[3] = 1, [4] = 0
			-> countGame =	[3]	+	[4]	+	1
			-> countGame =	(1)	+	(0)	+	1
			-> countGame = 2
			-> 현재게임은 2
			countSet의 정의도 동일한 방식을 가져갑니다.
		countSet = 현재 세트 == [5] + [6] + 1
		*/
		
		boolean loop = true;
		while (loop) {
			
			String[] terms = {"유저 포인트", "컴퓨터 포인트", "유저 게임 포인트", "컴퓨터 게임 포인트", "유저 세트 포인트", "컴퓨터 세트 포인트", "현재 게임", "현제 세트"};
			
			System.out.println("띄어쓰기 없이 아래 순서로 변수를 띄어쓰기 없이 아래와 같이 입력 부탁드립니다.\r\n"
								+ "p1포인트,p2포인트,p1게임포인트,p2게임포인트,p1세트포인트,p2세트포인트");
		System.out.println("현재 변수 값은 아래와 같습니다.");
		
		for (int i=0; i<values.length; i++) {
			System.out.printf("%s: %d\r\n", terms[i], values[i]);
		}
		
//		String temp = "";
//		
//		for (int i=0; i<values.length; i++) {
//			if (i!=values.length-1) {
//				temp += values[i] + ", ";
//			} else {
//				temp += values[i];
//			}
//		}
//		System.out.println(temp);
		System.out.print("입력: ");
		
		String text = scan.nextLine();
		String[] string = (text.split(","));
		
		int[] temp = new int[string.length + 2];
		
		for (int i=0; i<string.length; i++) {
			temp[i] = Integer.valueOf(string[i]);
		}
		
		System.out.println("입력하신 값을 확인 부탁드립니다.");
		System.out.printf("p1포인트: %d\r\n", temp[0]);
		System.out.printf("p2포인트: %d\r\n", temp[1]);
		System.out.printf("p1게임포인트: %d\r\n", temp[2]);
		System.out.printf("p2게임포인트: %d\r\n", temp[3]);
		System.out.printf("p1세트포인트: %d\r\n", temp[4]);
		System.out.printf("p2세트포인트: %d\r\n", temp[5]);
		System.out.println("1.예\t\t\t2.아니오");
		temp[6] = countGame = temp[2] + temp[3] + 1;
		temp[7] = countSet = temp[4] + temp[5] + 1;
		
		int input = scan.nextInt();
		scan.skip("\r\n");
		if (input == 2) {
			continue;
		} else if (input == 1);
			return temp;
		}
		return null;


		
		
		

		
	}

	private String pointName(int num1, int num2) {
		
		String[] pointNames = {"러브", "피프틴", "써티", "포티"	};
		String result;

		if (num1 == num2) {
			if (num1 < 3) {
				result = pointNames[num1] + "-올";
			} else {
				result = "듀스";
			}
		} else if (num1 > 3 || num2 > 3) {

			int diff = num1 - num2;
				
			if (diff == 1) {
				result = "애드 플레이어 1";
			} else if ( diff == -1 ) {
				result = "애드 플레이어 2";
			} else if (diff >= 2) {
				result = "게임 플레이어 1";
			} else {
				result = "게임 플레이어 2";
			}
		} else {
				result = pointNames[num1] + "-" + pointNames[num2];
		}

		return result;

	
	}


	private int matchId() {
		int result = 0;
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(PATH));
			String line = null;
			String temp = null;
			
			while ((line = reader.readLine()) != null) {
					temp = line;
			}
			result = Integer.parseInt(temp.split(",")[0])+1;
			
			
		} catch (Exception e) {
			System.out.println("TennisService.matchId");
			e.printStackTrace();
		}
		return result;
	}

	private void pointCheck(CharacterDTO dtoCharacter) {
		
		String p1 = dtoCharacter.getName();
		String p2 = "Player2";
		
		int matchNum = matchId();
		
		if (me.point > 3 && me.point - cpu.point > 1) {
			temp +=  matchNum + "," + countSet + "," + countGame + "," + me.point + "," + cpu.point + "\r\n";
			me.pointGame++;
			me.point = 0;
			cpu.point = 0;
			System.out.println();
			System.out.printf("%d세트 %d게임의 승자는 %s입니다.\r\n", countSet, countGame, p1);
			System.out.printf("현재 게임 스코어는 [%d-%d] 입니다.\r\n", me.pointGame, cpu.pointGame);
			System.out.println("다음 게임을 시작합니다.");
			System.out.println("[확인]");
			countGame++;
			countTotalServe = 1;
			scan.nextLine();
		}
		
		if (cpu.point > 3 && cpu.point - me.point > 1) {
			temp +=  matchNum + "," + countSet + "," + countGame + "," + me.point + "," + cpu.point + "\r\n";


			cpu.pointGame++;
			me.point = 0;
			cpu.point = 0;
			System.out.println();
			System.out.printf("%d세트 %d게임의 승자는 %s입니다.\r\n", countSet, countGame, p2);
			System.out.printf("현재 게임 스코어는 [%d-%d] 입니다.\r\n", me.pointGame, cpu.pointGame);
			System.out.println("다음 게임을 시작합니다.");
			System.out.println("[확인]");
			countGame++;
			countTotalServe = 1;
			scan.nextLine();
		}
		

		if (me.pointGame > 5 && me.pointGame - cpu.pointGame > 1) {
			
			me.pointSet++;

			me.point = 0;
			me.pointGame = 0;
			cpu.point = 0;
			cpu.pointGame = 0;
			System.out.println();
			System.out.printf("%d세트의 승자는 %s입니다.\r\n", countSet, p1);
			System.out.printf("현재 세트 스코어는 [%d-%d] 입니다.\r\n", me.pointSet, cpu.pointSet);
			System.out.println("다음 세트를 시작합니다.");
			System.out.println("[확인]");
			countServe = 1;
			countGame = 1;
			countSet++;
			scan.nextLine();
			
			
		} else if (cpu.pointGame > 5 && cpu.pointGame - me.pointGame > 1) {
			
			cpu.pointSet++;

			me.point = 0;
			me.pointGame = 0;
			cpu.point = 0;
			cpu.pointGame = 0;
			System.out.println();
			System.out.printf("%d세트의 승자는 %s입니다.\r\n", countSet, p2);
			System.out.printf("현재 세트 스코어는 [%d-%d] 입니다.\r\n", me.pointSet, cpu.pointSet);
			System.out.println("다음 세트를 시작합니다.");
			System.out.println("[확인]");
			countServe = 1;
			countGame = 1;
			countSet++;
			scan.nextLine();
			
		}
		
		if (dto.getSet() == 1) {
			if (me.pointSet > 1) {
				System.out.println();
				System.out.printf("%s이(가) [%d-%d]로 매치를 승리했습니다.\r\n", p1, me.pointSet, cpu.pointSet);
				System.out.println(temp);
			} else if (cpu.pointSet > 1) {
				System.out.println();
				System.out.printf("%s이(가) [%d-%d]로 매치를 승리했습니다.\r\n", p2, cpu.pointSet, me.pointSet);
				System.out.println(temp);
			}
		}
		
		if (dto.getSet() == 2) {
			if (me.pointSet > 2) {
				System.out.println();
				System.out.printf("%s이(가) [%d-%d]로 매치를 승리했습니다.\r\n", p1, me.pointSet, cpu.pointSet);
				System.out.println(temp);
			} else if (cpu.pointSet > 2) {
				System.out.println();
				System.out.printf("%s이(가) [%d-%d]로 매치를 승리했습니다.\r\n", p2, cpu.pointSet, me.pointSet);
				System.out.println(temp);
			}
		}
		
		
	}


	public String recordName(CharacterDTO characterDTO) {
		
		boolean loop = true;
		while (loop) {
			
			System.out.println("기록할 이름을 입력해주세요.");
			System.out.print("이름: ");
			String name = scan.nextLine();

			if (name.length() < 1 || name.length() > 10) {
				System.out.println("이름은 1자에서 10자 사이로 입력 부탁드립니다.");
				recordName(characterDTO);
			}
			
			System.out.printf("입력하신 이름이 %s이(가) 맞습니까?\r\n", name);
			System.out.println("1.예	2.아니오");
			
			int input = scan.nextInt();
			scan.skip("\r\n");
			
			if(input == 1) {
				
				dtoMatch.setNo(matchId());
				Calendar now = Calendar.getInstance();
				Calendar date = Calendar.getInstance();
				date.set(Calendar.YEAR, now.get(Calendar.YEAR));
				date.set(Calendar.MONTH, now.get(Calendar.MONTH));
				date.set(Calendar.DATE, now.get(Calendar.DATE));
				dtoMatch.setDate(date);
				dtoMatch.setName(name);
				dtoMatch.setCharacter(Integer.parseInt(characterDTO.getNo()));
				dtoMatch.setMePointSet(me.pointSet);
				dtoMatch.setCpuPointSet(cpu.pointSet);
				

				daoMatch.add(dtoMatch);
				dao.add(temp);
				temp = "";
				view.completeRecordGame();
				me.pointSet = 0;
				cpu .pointSet = 0;
				loop = false;
				
			} else if (input == 2) {
				
				continue;
			}
		}
		return "";
		
	}

	
}

