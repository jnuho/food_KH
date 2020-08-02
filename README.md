# food_KH
food ordering app

## model.vo 패키지
1. 데이터 정렬 방법
    User는 음식주문 사용자를 정의하는 클래스로 ```Comparable<User>``` 인터페이스를 상속

    User클래스에 있는 주문내역 ```Map<Food, Integer> orderList;```은 Food에 정의된 compareTo를 기반으로 자동정렬
    
    Food는 음식메뉴를 정의하는 클래스로 ```Comparable<Food>``` 인터페이스를 상속하여, 이들을 각각 key값으로 하는
        ```TreeMap<User, ?>``` 혹은 ```TreeMap<Food, ?>``` 컬렉션 사용 시, User, Food 클래스 에서 정의한 
        오버라이드 메소드 compareTo 에 의해 key값(User, Food)으로 TreeMap의 데이터 정렬(natural ordering)
        
    natural ordering 대신에 TreeMap 생성인자로 Comparator inner class를 전달하여 정렬방법 변경
    ```java
    Map<Food, Integer> orderList = new TreeMap<Food, Integer>(new Comparator<Food>(){
        @Override
        public int compare(Food o1, Food o2){
            return o1.getMenuPrice() - o2.getMenuPrice();
        }
    });
    ```
    FoodMenu는 음식메뉴 List를 정의하는 클래스로, 이 List는 Collections의 sort를 이용하여 정렬
    ```java
    List<Food> foodMenuList;
    ```
    
    Admin클래스에 있는 메뉴별 매출(salesResult)은 TreeMap의 자동정렬 특성과, Food의 compareTo에 정의된 
    규칙에 따라, 메뉴 알파벳 순 정렬 하였습니다. 다만 매출량을 조회시에 가장 많이 팔린 개수(Integer) 기준으로
    정렬은, Map의 Value값을 기준으로 정렬할 수 없기 때문에, 
    ```java
    List<Map.Entry<Integer, Food>> sortedSales;
    ```
    에 데이터를 넣고, Collections.sort으로 내림차순 정렬 했습니다.

2. 데이터 중복 여부
    User, Food 클래스에 equals와 hashCode 오버라이드 함수로 데이터를 컬렉션에 저장시 중복 방지

3. 직렬화
    User, Food, FoodMenu는 Object input/output 스트림으로 파일에 저장시
    직렬화를 위해 Serializable 인터페이스를 상속

## dao 패키지
    Constants클래스에 각종 상수 (public final static) 정의
    
    UserRepository 클래스는
        User객체 List, Food 메뉴 List, 읽기/쓰기용 User.dat 파일 객체, 
        User의 핸드폰번호(화면에서 로그인,음식주문,메뉴관리 등을 사용 중인 User)등 field 정의.
        이 데이터들을 화면에서 발생하는 event에 따라 변경. Event 발생시 UserRepository에
        저장된 데이터를 직접 변경하거나 UserRepository가 정의하고있는 메소드를 통해 변경.
        어플리케이션 시작시 User.dat 파일에 데이터를 read, 종료시 write.
        회원가입시 User의 입력 비밀번호를 Hash 알고리즘으로 암호화. (링크 참고)
        
## gui.layout 패키지
    InitPageFrame 메인화면의 디자인 component 생성 및 이벤트 정의.
        로그인과 로그아웃 버튼 이벤트는 event패키지의 SignInEventHandler와 SignOffEventHandler로 정의
        메인화면에서 메뉴 및 회원가입 버튼 클릭시,
        해당 메뉴로 넘어가기 위해 CardLayout에 4장의 카드(메인화면, 전체메뉴화면, 주문내역화면,회원가입화면)를 
        메인화면(JSplitPane)과 각각의 JPanel (FoodMenuPageCard, OrderViewPageCard, SignUpPageCard) 정의.
        메인화면에서 음식카테고리(면,탕,밥) 선택시, 세부 음식 메뉴를 CardLayout의 3장의 카드 패널로 보여줌
        음식 메뉴 정렬은 Food에 compareTo의 natural comparison에 따라 정렬되어 테이블에 보여짐.
        
        로그인한 유저는 음식 카테고리 3가지 중 하나를 선택하면, 오른쪽에 해당 카테고리의 음식메뉴를 보여줌
        테이블에서 해당 메뉴를 클릭하고, 수량, 결제방법 등을 선택해서 음식추가하면, 오른쪽 테이블에 메뉴가 추가됨
        추가 아이템을 삭제하려면 보라색 휴지통 버튼으로 삭제.
        메뉴 추가 완료됐으면, 맨아래 '주문하기'버튼을 눌러 최종 결제완료.
        
        관리자화면은 버튼 클릭시, 새로운 창이 생성되고, 2장의 카드(총 매출내역, 메뉴관리-추가 수정 삭제)로 구성됨
        관리자 화면에서 음식 메뉴 수정시에, 메인 화면에서도 메뉴가 같이 수정되도록
        ```DefaultTableModel.fireTableDataChanged();``` 메소드를 호출.
        
    FoodMenuPageCard
        전체 음식메뉴를 보여주는 기능
    OrderViewPageCard
        로그인된 유저의 주문내역을 보여주는 기능
        
    SignUpPageCard
        회원가입하는 페이지.
        
    AdminPageFrame
        관리자로 로그인됐을때만 창을 열수 있음.
        메인화면과 별개의 화면이 생성됨.
        매출내역은 팔린 음식과 갯수
    
## gui.event 패키지
    로그인 버튼 클릭시, SignInEventHandler가 이벤트를 처리. 버튼 이벤트 추가시에, UserRepository 및
    폰번호, 비밀번호 텍스트를 매개변수로 넘겨 데이터 값이 변경되도록 함.
    로그오프 버튼 클릭시에는 SignOffEventHandler로가 이벤트를 처리, 로그인과 동일하게 매개변수를 통해 데이터 변경.

* 매출내역, 주문내역 날짜별로 누적 혹은 날짜별 검색 날짜별로 월별 혹은 검색가능하게(구현 예정)
```java
Map<GregorianCalendar, Map<Food, Integer>> orderHistory; //User
Map<GregorianCalen dar, Map<Food, Integer>> salesHistory; //Admin
```

* [RowFilter Api](https://stackoverflow.com/questions/22066387/how-to-search-an-element-in-a-jtable-java)
