                  Morimoto Traffic Simulator ver.0.21
        for RoboCupRescue Prototype Simulation System ver.0.39

                            May. 11, 2002


�ܥץ����ϡ�RoboCup2002 in Fukuoka/Busan �� RoboCupRescue
Simulation League �Ǥλ��Ѥ������Ū�˳�ȯ���줿�� RoboCupRescue
Prototype Simulation System �Ѹ��̥��ߥ�졼���Ǥ���

�ܥץ����� Ver.0 �Ǻ���Ƥ����¸�Υ���������Ȥ��ؤ�ͤ���������
��ή���ߥ�졼���Ȥθߴ�����Ż뤷�Ƥ��ޤ����������ˡ�����������Ⱥ�����
���ʤ���̱ƶ��Τ����Τ�����ޤ�����������ή���ߥ�졼���ǳ����̤�˰�
���Ƥ��ʤ���Τ��ܥץ����Ǥ⳵�Ͱ����ޤ���


I. Installation

To make this simulator, you simply type the following command:

    % make

    NOTE: JDK 1.3.0 is required


II. Usage

Synopsis is as follows:

    java traffic.Main [ hostname [ port ] ]

    hostname : kernel host name (default: localhost)
    port     : kernel port (default: 6000)


III. �¹ԴĶ�

1/10 ��ǥ�Υ��ߥ�졼�����Ǥϡ��׻���٤��礭�����ᡤ�ץ�����
�ʤ��Ȥ�3��Υޥ���ˡ��ʲ��Τ褦�˿���ʬ����ɬ�פ�����ޤ���

  ���ߵ���/������/ƻϩ�����⥨���������
  ����̱����������ȡ��кҥ��ߥ�졼��
  �������ͥ롤����¾�Υ��ߥ�졼�� (�ܥץ�����ޤ�)

�кҤȸ��̤Ϸ׻���������Ӥ��ŤʤäƤ��ꡤʬ��������ɬ�פ�����ޤ�������
������ȷ��ȥ��֥��ߥ�졼�����Ϸ׻���������Ӥ��ۤʤ뤿�ᡤ��̱��������
��ȤȲкҥ��ߥ�졼���� 1 ��Ƿ׻����Ƥ����ꤢ��ޤ���

�ʤ����ܥץ����ϰʲ��� PC 3 ���Ȥ�ư����ǧ���ޤ�����

    CPU : Pentium III 930MHz
    MEM : 256MB
    OS  : Linux (Vine 2.1.5)
    JDK : 1.3.0


IV. ����������ȳ�ȯ��ɬ�פʺ���¤ξ���

(1) �ܥץ����� AK_MOVE/LOAD/UNLOAD ��������ޤ��� AK ���ޥ�ɤλ���
�ϴ�¸�Υ��ߥ�졼����Ʊ�ͤǤ���

(1-1) ��ư���� (MovingObject �Τ�)
    Header: AK_MOVE
    Body:   routePlan : an array of IDs  (32 bit * number of route objects)
            0         : a sentinel of the array (32 bit)

��ư��ϩ routePlan �˱�äơ�����������Ȥ��ư�����ޤ�����ư��ϩ�ϡ���
���Ϥ�����Ȥ��� 1�İʾ�� MotionlessObject �� ID ���鹽������롤���ޤ�
�褦�ʥ����ȥޥȥ󤬼���������������θ�Ǥ���

             n          n
          ------>    <------ OrgBldg
      Road       Node
          <------    ------> DestBldg
             r          b

       ������� | Road | Node | OrgBldg
      ----------+------+------+----------
       ���߰��� | Road | Node | Building

       ���ϵ��� | n                | r                | b
      ----------+------------------+------------------+----------------------
       ��    ̣ | ���ܤ���Node��ID | ���ܤ���Road��ID | ���ܤ���Building��ID

      ��λ���� : ���Ƥξ���

(1-2) ����Ԥ�ߵ޼֤˾褻�� (AmbulanceTeam �Τ�)
    Header: AK_LOAD
    Body:   target : an ID (32 bit)

����� target ��ߵ޼֤˾褻�ޤ�������Ԥϵߵ����Ʊ�����ˤ��ʤ���Ф�
��ޤ��󡥤�������ƻϩ�Ȥ���ü����Ʊ�����֤Ȥߤʤ��ޤ���

(1-3) ����Ԥ�ߵ޼֤���ߤ� (AmbulanceTeam �Τ�)
    Header: AK_UNLOAD
    Body:   Nothing

�ߵ޼֤˾褻�Ƥ�������Ԥ�ߤ��ޤ����ߤ��줿����Ԥΰ��֤ϡ��ߵ����
Ʊ���ˤʤ�ޤ���

(2) �̲�β���

��ư��ϩ���ĺɤ���ڤ������硤����������ȤϤ���ƻϩ���̲�Ǥ������ĺ�
����ڤμ�������ߤ��ޤ���

(2-1) �ĺ�

ƻϩ���ĺɤⰯ������ή���ߥ�졼����Ʊ�ͤǤ��ꡤ�ʲ��Τ褦�������ޤ���

    lineWidth             := width / (linesToHead + linesToTail)
    road.blockedlines     := floor(road.block / road.lineWidth / 2 + 0.5)
    road.aliveLinesTo...  := max(0, road.linesTo... - road.blockedLines)
    road.isPassableToHead :- road.aliveLinesToHead >= 1
                          or movingObject.positionExtra < road.length / 2
    road.isPassableToTail :- road.aliveLinesToTail >= 1
                          or movingObject.positionExtra > road.length / 2

�ĺɤ�ƻϩ�ο���� 1 ����¸�ߤ��뤳�Ȥˤ��ޤ����ޤ���������ʣ��������
�ϡ���¦�μ����������ĺɤ��뤳�Ȥˤ��ޤ��� isPassableTo... �� true ��
��������Ȥϡ�ƻϩ����¦���ĺɤ��Ƥ��ʤ����������뤫�����뤤�ϴ����ĺɤ�
�ۤ������֤ˤ���������̣���ޤ���

�ĺɤ���ƻϩ���̲�β��ݤϡ��֤�ͤ�Ʊ�ͤ�Ƚ�ꤷ�ޤ���

(2-2) ����

�ܥץ����ϡ�����������Ȥ������μ֤Ȥμִֵ�Υ��������ݤ��Ĥİ�ư
�����ޤ������Τ��ᡤ�֤�ͤ�̩������Ƚ��ڤ�ȯ�����ޤ���

�����μ֤ޤǤκ�û�μִֵ�Υ�� MIN_SAFE_DISTANCE_BETWEEN_CARS��
�����οͤޤǤκ�û�δֳ֤� MIN_SAFE_DISTANCE_BETWEEN_CIVILIAN �Ǥ�
(cf. Constants.java)��

���������� (�۵޼�) ��ͥ�褷�ơ���̱�ϼ֤ˤҤ���ʤ��褦����տ�����ư��
��ȹͤ����֤ϻ�̱��̵�뤷�ư�ư�Ǥ��ޤ����ޤ�����������ߤ��Ƥ���֤��
��������Ǥ⡤�̤μ����������Ƥ���м������ѹ��Ǥ��뤿�ᡤ�̲᤹�뤳��
���Ǥ��ޤ���

(3) ����¾

(3-1) �֤κǹ�®�٤� MAX_VELOCITY_PER_SEC [mm/sec]
(cf. Constants.java) �Ǥ��ꡤ 1 cycle �ǰ�ư�Ǥ����Ĺ��Υ�Ϲ⡹ 
MAX_VELOCITY_PER_SEC * 60 [mm] �Ǥ���

(3-2) Ver.0�Ǥϰ�ư����֤϶۵޼�ξ�ΤߤʤΤǡ�����, �����ޤ����¤Ϲͤ�
�ޤ���
    
(3-3) ��������ή���ߥ�졼���ǻȤ��Ƥ��ʤ��ä����ʬΥ�ӡ���Լ���ƻϩ
������������Ƥ��ʤ��ä� carsPassTo...�� humansPassTo...�ϡ��ܥץ����
�Ǥⰷ���ޤ���


V. ��꿼������������ȳ�ȯ�Τ����

(1) ���ߥ�졼�����γ���

�ܥץ����ν����ǥ����Ȥʤ�Τϡ�ƻϩ�˼����γ�ǰ�����Τˤ��뤳�ȤǤ���
����������Ȥϡ���ʪ����ˤ������Τ����ơ���˼�����ˤ��ޤ����ܥץ�
����ब�����ʤ������ϡ�

  ��������ư����
  ��ʪ������
  ����Ԥ�褻��/�ߤ�
  �������̲᤹��
  �ĺɤ���򤹤�
  ��߼�ξ���ɤ��ۤ�
  ž�󤹤�
  ��Ū�������˼֤�¦�μ����˴󤻤�
  ��ʪ����ϩ��˽Ф�

�Ǥ��ꡤ���Τ������� 6 �Ĥν����ϡ������ѹ������˵��夵��ޤ���
Prototype Simulation System �λ��;塤����������Ȥˤϼ�ʬ���ɤμ������
���뤫���Τ�ѤϤ���ޤ��󤬡����ڤβ�����äΤ���ˤϡ�������ռ�����
����������ȥץ���ߥ󥰤����פˤʤ�ȹͤ����ޤ������ξϤǤϼ�������
���ˡ��ܥץ���ब�Ԥ����ߥ�졼�����γ��פ��������ޤ���

(2) ������ΰ�ư

����������Ȥΰ�ư�ϡ�������ΰ�ư�ȼ����ѹ��η����֤��ˤ�äƷ׻������
�����Ĥޤꡤ�㳲ʪ (��Ū�Ϥ��ư���������ˤ��������μ�ξ��ޤ�) �ޤǼ�
�����ľ�ʤ����㳲ʪ��ۤ��뤿��˼������ѹ����롤�Ȥ�����������Ū�Ϥ�
���ɤ��夯�ޤǷ����֤��ޤ���

����������ȤϾ㳲ʪ�μ����ǳμ¤���ߤǤ���褦�ˡ�®�٤��ޤ��ư�ư����
����®�٤ϡ����ߤ�®�٤˲�®�٤�ä��뤳�ȤǷ��ꤷ����®�� a �ϰʲ�����
���� (1) �� a �ˤĤ��Ʋ򤯤��Ȥˤ����ޤ���

    dx  : distance from the forward object
    ma  : maximum acceleration
    msd : minimum safe distance to forward object
    v   : velocity
    a   : acceleration
    safeDistance = (v + a)^2 / (2 * ma) + msd
    dx - safeDistance = v + a                  ... (1)

    NOTE: t = (v + a) / ma : ��ߤޤǤˤ��������
          (v + a) / 2 * t  : ��ߤޤǤ˰�ư�����Υ

�֤β�®�٤ˤ����¤����ꡤ�֤���߾��֤���ǹ�®�٤ˤʤ�ޤ� (���εդ�Ʊ
��) �ˡ����ʤ��Ȥ� ACCELERATING_SEC [sec] (cf. Constants.java) ���פ���
������̱���⤤�ư�ư���뤿�ᡤ��®�٤����¤Ϥ���ޤ���

�� cycle �ν�®�ˤϡ����� cycle �κǽ�®�٤��Ѥ��ޤ�����������������
cycle ��Ϳ����줿����������Ȥι�ư���Ƥ�����ư�Ǥʤ��ä��ꡤ�������ؤ�
��ư�Ǥ�����ˤϡ�����ߤ����ȹͤ�����®�� 0 [mm/sec] �ˤ��ޤ��������
���ߥ�졼�����λ���γ�� 1 ʬ���Ʋ᤮�뤿��������ޤ���

(3) �������ѹ�

��˽Ҥ٤��褦�ˡ��ʲ��ι԰�

  �������̲᤹��
  �ĺɤ���򤹤�
  ��߼�ξ���ɤ��ۤ�
  ž�󤹤�
  ��Ū�������˼֤�¦�μ����˴󤻤�
  ��ʪ����ϩ��˽Ф�

�ϼ����ѹ��Ȥ��ƽ�������ޤ���

�����ѹ��ν����Ǥϡ��ѹ���θ���μ������γƼ�����ˡ������ߤβ��ݤ�Ƚ
�ꤷ�������߲�ǽ�ʤ�������ѹ����ޤ��������ߤ���ǽ�ʾ����Ȥϡ�����
��Ǥ�����������ȸ����ˤ���֤Ȥμִֵ�Υ���������ݤ��������Ǥ����ѹ�
����Τɤμ����ˤ������ʤ����ϡ��������ޤ��Ե����ޤ���

����礭�ʤ��뤤��Ʊ����ƻϩ (�ʹ������μ�������¿�����ޤ���������ƻϩ 
--- �Ĥޤꡤͥ��ƻϩ) �ȸ��� Node (ʬ��, ��ή��ޤ�) ������ȸƤӤ�
����

(4) ��ư��ϩ�Ȥ��ν���

���ߥ�졼�����ν������Ƥ����������뤿��ˡ���ư��ϩ�˱�������������
�Υ��᡼����ޤ��Ѥ��ƽҤ٤ޤ�����ư��ϩ�ϰʲ���ɽ�ε���ˤ�ä�ɽ������
����

   B        | N    | R
  ----------+------+------
   Building | Node | Road

�������ƤΥ��᡼���ϰʲ���ɽ�ε���ˤ�ä�ɽ�����ޤ���

   [ ]      | :    | ---> | +        | s          | m
  ----------+------+------+----------+------------+--------------------
   Building | Node | Lane | Blockade | self agent | other MovingObject

  case 1:
    route plan: {B}
      # ���⤷�ʤ�
      [s]        [s]
      before     after

  case 2:
    route plan: {... R}
    ���� AK_MOVE �������Ƥʤ�����Road ��ˤ���
      # ¾�μ֤�˸���ˤʤ�ʤ��褦�ˡ����ֳ�¦�μ����˴��
      ------->       ---s--->
      ---s--->       ------->
      <-------       <-------
      <-------       <-------
       before         after

  case 3:
    route plan: {... N}
    ���� AK_MOVE �������Ƥʤ�����Node ��ˤ���
      # ¾�μ֤�˸���ˤʤ�ʤ��褦�ˡ����ֳ�¦�μ����˴��
      ------->:       ------->s
      ------->s       ------->:
      <-------:       <-------:
      <-------:       <-------:
       before           after

  case 4:
    route plan: {B N}
      # N ����ФƤ���������˼����ѹ�����
             [s]                   [ ]
              |                     |
      ------->:------->      ------->s------->
      <-------:<-------      <-------:<-------
           before                  after

  case 5:
    route plan: {B N B}
      # N����ФƤ���������˼����ѹ������塤��Ū�η�ʪ������
             [s]                    [ ]                    [ ]
              |                      |                      |
      ------->:------->      ------->s------->      ------->:-------->
      <-------:<-------      <-------:<-------      <-------:<--------
              |                      |                      |
             [ ]                    [ ]                    [s]
           before                  after                more after

  case 6:
    route plan: {B N R} ({B R} is similar)
      # N ����ФƤ��� R��μ������λ����˼����ѹ�����
      [s]             [ ]
       |               |
       :------->       :s------>
       :<-------       :<-------
         before           after

  case 7:
    route plan: {... N B}
      # B ������
             [ ]            [s]
              |              |
      ------->s      ------->:
      <-------:      <-------:
        before         after

  case 8:
    route plan: {... N R ...}
      case 8-1: ... -> N -> R -> ...
        case 8-1-1: N ���� R ��ľ�ʲ�ǽ
          # �ʤ�
          s------->       :---s--->
          before           after

        case 8-1-2: N �� R �ؤθ���
          # N ����ФƤ��� R ��μ������λ����˼����ѹ������塤�ʤ�
           :------->       :s------>       :---s--->
          s:               :               :
            before           after         more after

        case 8-2:      ->    -> N  -> ...
                  ... <-  R <-    <-
        # N ����ФƤ��� R ��μ������λ����˼����ѹ������塤�ʤ�
        ------->s------->       ------->:------->       ------->:------->
        <-------:               <------s:               <---s---:
             before                    after                more after

  case 9:
    route plan: {... R N ...}
      case 9-1: ... -> R -> N -> ...
        case 9-1-1: R ���� N ��ľ�ʲ�ǽ
        # �ʤ�
        ---s--->:       ------->s
         before           after

        case 9-1-2: R �� �ĺɤ��Ƥ���
          case 9-1-2-1: �����ĺ� (ƻϩ���濴) ��᤮�����֤ˤ���
          # �ʤ�
          ---+s-->:       ---+--->s:
           before           after

          case 9-1-2-2: �ĺɤμ����ˤ���
          # ���ʤǤ���������˼����ѹ������塤�ʤ� (�ʤ���Ф��������)
          --s+--->:       ---+--->:       ---+---> :
          ------->:       --s---->:       ------->s:
           before           after         more after

        case 9-1-3: ��ưʪ�Τμ����ˤ���
        # ���ʤǤ���������˼����ѹ������塤�ʤ� (�ʤ���Ф��������)
        -s---m->:       -----m->:       -----m-> :
        ------->:       -s----->:       ------->s:
         before           after         more after

      case 9-2:      ->    -> R  -> ...
                ... <-  N <-    <-
      # N �˸����� R���ȿ�м�������Ʊ�����֤˼����ѹ������塤�ʤ� (== ž��)
      :-------s->       :--------->       : --------->
      :<---------       :<------s--       :s<---------
         before            after           more after

(5) AK_MOVE/LOAD/UNLOAD ����ͥ����

AK_MOVE ���ͥ��ǽ������ޤ���AK_LOAD/UNLOAD �ϡ��������åȤ�����Ԥ� 
AK_MOVE ���������ʤ��ä����Τ߹Ԥ��ޤ���

Prototype Simulation System Ver.0.31 �����ϡ�AK_LOAD/UNLOAD ��Misc
Simulator ���������Ƥ��ޤ�������Simulation System Ver.0.36 �Ǥϡ�
position, positionExtra, positionHistory �ץ�ѥƥ��ζ�����ɤ����ᡤ��
�̥��ߥ�졼�����������ޤ���


VI. �ܥץ����γ�ȯ/�ݼ��ɬ�פʾ���

(1) ����������Ȥΰ��֤˴ؤ���ʲ��Υץ�ѥƥ�

  MovingObject.position
  MovingObject.positionExtra
  MovingObject.positionHistory

�ϡ����̥��ߥ�졼���Τߤ����ꤷ��¾�Υ��ߥ�졼�����ѹ����ʤ����Ȥ�����
�Ȥ��ޤ������줬�ݾڤ���ʤ��ȡ����ߥ�졼�������ξ��֤ȡ��ܥץ���ब
�������ݤäƤ��륨��������Ȥξ��� (������®�٤ʤ�) �Ȥδ֤˰�������ʤ�
�ʤꡤ���ߥ�졼�����Υץ���ߥ󥰤�����ˤʤ�ޤ���

positionHistory �ϰ�������ή���ߥ�졼����Ʊ�ͤǤ��ꡤ��ϩ��� Node �Υ�
���ȤȤ���ɽ������ޤ���

(2) Constants.java ���������Ƥ�������ͤ��ѹ��ˤ�ꡤ���꤬�ѹ��Ǥ���
����

  ���ߥ�졼�����η׻�ñ�̻���	UNIT_SEC
  ��/�ͤ�®��				MAX_VELOCITY_PER_SEC
					MAX_CIV_VELOCITY_PER_SEC
  �֤κǹ�®�٤ޤǤˤ��������		ACCELERATING_SEC
  ��/�ͤ���߻��ΰ����ʼִֵ�Υ		MIN_SAFE_DISTANCE_BETWEEN_CARS
					MIN_SAFE_DISTANCE_BETWEEN_CIVILIAN
  �ʹ�����				DRIVING_DIRECTION_IS_LEFT
  �׻����Ǥ��ڤ���� (cf. VI (3-3))	CALCULATING_LIMIT_MILLI_SEC

(3) ���ߥ�졼������ή��

���ߥ�졼�����ϡ��ޤ���
  traffic.Simulator.Simulator(InetAddress kernelAddress, int kernelPort)
�ˤ��������Ԥ����ʸ塤
  traffic.Simulator.simulate()
�򷫤��֤����ȤǿʹԤ��ޤ���

(3-1) traffic.Simulator.simulate() �γ���

  ����������Ȥι�ư���Ƥ����	traffic.io.receiveCommands()
  AK_MOVE �����		traffic.Simulator.move()
  AK_LOAD/UNLOAD �����		traffic.loadUnload()
  ���ߥ�졼������̤�����	traffic.io.sendUpdate()
  ���ߥ�졼������̤����	traffic.io.receiveUpdate()

(3-2) ����������Ȥι�ư���ƤΥ����å�

����������Ȥι�ư���Ƽ������ˡ�
  traffic.WorldModel.parseCommands(int[] data)
�ˤ�ꡤ��ư���Ƥβ����������Υ����å���Ԥ��ޤ�����ư���Ƥ����ͤ�����
���Ƥ��ʤ����ϡ������ʳ��ǤϤ����ޤ���

(3-3) AK_MOVE �ν����γ���

�ƥ�������κǽ�ˡ�
  traffic.object.MovingObject.initializeEveryCycle()
�ˤ�ꡤ��ư��ϩ������������UNIT_SEC (cf. VI (3-2)) ñ�̤ǥ����������
�ΰ�ư�򥷥ߥ�졼�Ȥ��ޤ���1 ñ�̻�����Υ��ߥ�졼�����γ��פϰʲ���
�Ȥ���Ǥ���

  1) ���ߥ�졼�������Ǥ��ڤ�Ƚ��
    CALCULATING_LIMIT_MILLI_SEC

  2) ��ư�����������Ⱦ㳲ʪ��Ĵ�٤�
    traffic.object.MovingObject.setMotionlessObstructionAndMovingLaneList()

  3) ��ư�������ˤ���������ξ��Ĵ�٤�
    traffic.object.MovingObject.setMovingObstruction()

  4) �㳲ʪ�μ���˱����ƥ���������Ȥ��ʬ������
    traffic.Simulator.sortByObstruction()

  5) ������ξ�˹Ԥ�����ˤޤ�Ƥ��륨��������Ȥ��ư������
    traffic.Simulator.m_waitingNoChangeList
    traffic.Simulator.moveBeforeForwardMvObj(MovingObject MovingObject mv)

  6) �ĺɡ������ѹ���ž��μ����ˤ��륨��������Ȥ��ư������
    traffic.Simulator.m_waitingMap
    traffic.Simulator.dispatch(ArrayList follows)

  7) ��ʪ����������Ū�Ϥμ����ˤ��륨��������Ȥ��ư������
    traffic.Simulator.m_noWaitingList
    traffic.object.MovingObject.move()

  8) �㳲ʪ���ʤ�����������Ȥ��ư������
    traffic.Simulator.m_noWaitingNoChageList
    traffic.object.MovingObject.move()

1) �ϡ����ߥ�졼������̤��襵�����롤�μ¤˥����ͥ�������뤿��Ρ�
�������̽��֤Ǥ����ǽ�ο�������ȡ�¿���Υ���������Ȥ�Ĺ��Υ��ư����
�褦�ʾ��ˡ��׻����Ǥ��ڤ뤳�Ȥ�����ޤ���

6) �� traffic.Simulator.dispatch(ArrayList follows) �Ǽ����ѹ����륨������
��Ȥο��ϡ�1 �Ĥξ㳲ʪ�ˤĤ���1 ñ�̻��������� 1 ����������Ȥ˸¤ä�
���ޤ���


VII. License Of Morimoto Traffic Simulator

(1) Neither the RoboCupRescue committee nor development staffs of this
program provide warranty.  Use the software at your own risk.

(2) Copyright of all program code and documentation included in source
or binary package of this program belongs to Takeshi Morimoto.

(3) You can use this program for research and/or education purpose
only, commercial use is not allowed.


VIII. Author

Takeshi MORIMOTO
Ikuo Takeuchi Laboratory
Department of Computer Science
The University of Electro-Communications

Additional information can be found in:
    http://ne.cs.uec.ac.jp/~morimoto/rescue/traffic/

Mail bug reports and suggestions to:
    morimoto@takopen.cs.uec.ac.jp
