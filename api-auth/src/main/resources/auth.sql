CREATE TABLE IF NOT EXISTS `me_member` (
                          `id` VARCHAR(22) NOT NULL COMMENT '회원 아이디',
                          `login_id` VARCHAR(128) NOT NULL COMMENT '로그인 아이디 (Portal: 전화번호, 코드샵: 이메일)',
                          `login_password` VARCHAR(512) NULL COMMENT '로그인 패스워드 (nullable 가능, varchar(255) → varchar(512) 변경)',
                          `login_password_salt_key` VARCHAR(255) NULL COMMENT '로그인 패스워드 솔트 키',
                          `status` CHAR(1) NOT NULL COMMENT '상태 (I: 초대, A: 활성, D: 비활성)',
                          `updated_at` DATETIME NOT NULL COMMENT '수정일시 (yyyy-MM-dd HH:mm:ss)',
                          `updated_by` VARCHAR(255) NOT NULL COMMENT '수정자',
                          `version` INT NOT NULL COMMENT '낙관적 락 버전',
                          `created_at` DATETIME NOT NULL COMMENT '생성일시 (yyyy-MM-dd HH:mm:ss)',
                          `created_by` VARCHAR(255) NOT NULL COMMENT '생성자',
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `uk_member_login_id` (`login_id`)
)
    ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_general_ci
COMMENT = '회원';


CREATE TABLE IF NOT EXISTS `me_member_connect` (
                                                   `id` INT NOT NULL AUTO_INCREMENT COMMENT '회원 연결 아이디',
                                                   `member_id` VARCHAR(22) NOT NULL COMMENT '회원 아이디',
                                                   `connect_type_code` VARCHAR(512) NOT NULL COMMENT '연결 유형 (구글, 네이버, 카카오 등)',
                                                   `connected_id` VARCHAR(255) NOT NULL COMMENT '연결된 계정의 고유 식별자 (구글, 네이버, 카카오 등)',
                                                   `name` VARCHAR(512) NULL COMMENT '(Encrypted) 이름',
                                                   `email` VARCHAR(512) NULL COMMENT '(Encrypted) 이메일',
                                                   `mobile` VARCHAR(512) NULL COMMENT '(Encrypted) 휴대폰번호',
                                                   `picture` VARCHAR(512) NULL COMMENT '프로필 사진 URL',
                                                   `gender` CHAR(1) NULL COMMENT '성별 (M: 남성, F: 여성, U: 알 수 없음)',
                                                   `age` VARCHAR(10) NULL COMMENT '연령대 (e.g., 20~29)',
                                                   `birthday` VARCHAR(4) NULL COMMENT '생일 (MMDD, e.g., 0923)',
                                                   `birthyear` VARCHAR(4) NULL COMMENT '출생년도 (YYYY, e.g., 1973)',
                                                   `locale` VARCHAR(8) NULL COMMENT '언어 및 지역 코드 (e.g., ko_KR)',
                                                   `cipher_key_id` VARCHAR(22) NOT NULL COMMENT '암호화 키 아이디 (co_cipher_key)',
                                                   `status` CHAR(1) NOT NULL COMMENT '상태 (I: 초대, A: 활성, D: 비활성)',
                                                   `updated_at` DATETIME NOT NULL COMMENT '수정일시 (yyyy-MM-dd HH:mm:ss)',
                                                   `updated_by` VARCHAR(255) NOT NULL COMMENT '수정자',
                                                   `version` INT NOT NULL DEFAULT 0 COMMENT '낙관적 락 버전',
                                                   `created_at` DATETIME NOT NULL COMMENT '생성일시 (yyyy-MM-dd HH:mm:ss)',
                                                   `created_by` VARCHAR(255) NOT NULL COMMENT '생성자',
                                                   PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci
    COMMENT = '회원 연결 정보';

CREATE TABLE IF NOT EXISTS `me_member_identification` (
                                                                 `id` INT NOT NULL AUTO_INCREMENT COMMENT '패스 본인 확인 아이디',
                                                                 `name` VARCHAR(512) NULL COMMENT '(Encrypted) 이름',
                                                                 `mobile_phone_number` VARCHAR(512) NULL COMMENT '(Encrypted) 휴대폰번호',
                                                                 `email` VARCHAR(512) NULL COMMENT '(Encrypted) 이메일',
                                                                 `ci` VARCHAR(512) NULL COMMENT '(Encrypted) CI',
                                                                 `birthday` DATE NULL COMMENT '생년월일',
                                                                 `gender` CHAR(1) NULL COMMENT '성별 (M: 남성, W: 여성, U: 알 수 없음)',
                                                                 `foreign_yn` CHAR(1) NULL COMMENT '외국인 여부 (Y: 외국인, N: 내국인)',
                                                                 `name_sha` VARCHAR(512) NULL COMMENT '(Hashed) 이름',
                                                                 `mobile_phone_number_sha` VARCHAR(512) NULL COMMENT '(Hashed) 휴대폰번호',
                                                                 `email_sha` VARCHAR(512) NULL COMMENT '(Hashed) 이메일',
                                                                 `ci_sha` VARCHAR(512) NULL COMMENT '(Hashed) CI',
                                                                 `cipher_key_id` VARCHAR(22) NULL COMMENT '암호화 키 아이디 (co_cipher_key)',
                                                                 `mobile_carrier_code` VARCHAR(10) NOT NULL COMMENT '이동 통신사 코드 (CC. 공통)',
                                                                 `member_id` VARCHAR(22) NULL COMMENT '회원 아이디',
                                                                 `status` CHAR(1) NOT NULL COMMENT '상태 (R: 요청, C: 완료, A: 취소, E: 만료)',
                                                                 `updated_at` DATETIME NOT NULL COMMENT '수정일시 (yyyy-MM-dd HH:mm:ss)',
                                                                 `updated_by` VARCHAR(255) NOT NULL COMMENT '수정자',
                                                                 `version` INT NOT NULL DEFAULT 0 COMMENT '낙관적 락 버전',
                                                                 `created_at` DATETIME NOT NULL COMMENT '생성일시 (yyyy-MM-dd HH:mm:ss)',
                                                                 `created_by` VARCHAR(255) NOT NULL COMMENT '생성자',
                                                                 PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci
    COMMENT = '패스 본인 확인 정보';

CREATE TABLE IF NOT EXISTS `me_member_identification_status` (
                                                                 `id` INT NOT NULL AUTO_INCREMENT COMMENT '본인 확인 상태 아이디',
                                                                 `identification_id` INT NOT NULL COMMENT '본인 확인 아이디',
                                                                 `status` CHAR(1) NOT NULL COMMENT '상태 (R: 요청, C: 완료, E: 취소)',
                                                                 `created_at` DATETIME NOT NULL COMMENT '등록일시',
                                                                 `created_by` VARCHAR(255) NOT NULL COMMENT '등록자 아이디',
                                                                 PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci
    COMMENT = '본인 확인 상태 로그';

CREATE TABLE IF NOT EXISTS `me_member_login_history` (
                                                         `id` INT NOT NULL AUTO_INCREMENT COMMENT '회원 로그인 이력 아이디',
                                                         `member_id` VARCHAR(22) NOT NULL COMMENT '회원 아이디',
                                                         `login_at` DATETIME NOT NULL COMMENT '로그인 일시',
                                                         `login_history_user_agent` VARCHAR(255) NOT NULL COMMENT '로그인 이력 사용자 에이전트',
                                                         `login_history_ip` VARCHAR(64) NOT NULL COMMENT '로그인 이력 사용자 IP 주소',
                                                         `login_success_yn` CHAR(1) NOT NULL COMMENT '로그인 성공 여부 (Y: 성공, N: 실패)',
                                                         `created_at` DATETIME NOT NULL COMMENT '등록일시',
                                                         `created_by` VARCHAR(255) NOT NULL COMMENT '등록자 아이디',
                                                         PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci
    COMMENT = '회원 로그인 이력';


CREATE TABLE IF NOT EXISTS `me_member_out` (
                                                      `id` INT NOT NULL AUTO_INCREMENT COMMENT '아이디',
                                                      `member_id` VARCHAR(22) NOT NULL COMMENT '회원 아이디',
                                                      `out_at` DATETIME NOT NULL COMMENT '탈퇴 일시',
                                                      `out_reason` VARCHAR(1024) NOT NULL COMMENT '탈퇴 이유',
                                                      `updated_at` DATETIME NOT NULL COMMENT '수정일시 (yyyy-MM-dd HH:mm:ss)',
                                                      `updated_by` VARCHAR(255) NOT NULL COMMENT '수정자',
                                                      `version` INT NOT NULL DEFAULT 0 COMMENT '낙관적 락 버전',
                                                      `created_at` DATETIME NOT NULL COMMENT '생성일시 (yyyy-MM-dd HH:mm:ss)',
                                                      `created_by` VARCHAR(255) NOT NULL COMMENT '생성자',
                                                      PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci
    COMMENT = '회원 탈퇴 이력';

CREATE TABLE IF NOT EXISTS `me_member_password_change_history` (
                                                                          `id` INT NOT NULL AUTO_INCREMENT COMMENT '회원 비밀번호 변경 이력 아이디',
                                                                          `member_id` VARCHAR(22) NOT NULL COMMENT '회원 아이디',
                                                                          `login_password` VARCHAR(255) NOT NULL COMMENT '비밀번호',
                                                                          `login_password_salt_key` VARCHAR(255) NOT NULL COMMENT '비밀번호 salt_key',
                                                                          `change_at` DATETIME NOT NULL COMMENT '비밀번호 변경 일시',
                                                                          `cipher_key_id` VARCHAR(22) NOT NULL COMMENT '암호화 키 아이디',
                                                                          `updated_at` DATETIME NOT NULL COMMENT '수정일시 (yyyy-MM-dd HH:mm:ss)',
                                                                          `updated_by` VARCHAR(255) NOT NULL COMMENT '수정자',
                                                                          `version` INT NOT NULL DEFAULT 0 COMMENT '낙관적 락 버전',
                                                                          `created_at` DATETIME NOT NULL COMMENT '생성일시 (yyyy-MM-dd HH:mm:ss)',
                                                                          `created_by` VARCHAR(255) NOT NULL COMMENT '생성자',
                                                                          PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci
    COMMENT = '회원 비밀번호 변경 이력';

CREATE TABLE IF NOT EXISTS `me_member_session` (
                                                          `id` VARCHAR(22) NOT NULL COMMENT '아이디',
                                                          `member_id` VARCHAR(22) NOT NULL COMMENT '회원 아이디',
                                                          `login_id` VARCHAR(128) NOT NULL COMMENT '로그인 아이디',
                                                          `login_session_id` VARCHAR(22) NOT NULL COMMENT '세션 아이디',
                                                          `refresh_token` VARCHAR(22) NOT NULL COMMENT 'Refresh token',
                                                          `expiration` DATETIME NOT NULL COMMENT 'Refresh token 만료 일시',
                                                          `updated_at` DATETIME NOT NULL COMMENT '수정일시 (yyyy-MM-dd HH:mm:ss)',
                                                          `updated_by` VARCHAR(255) NOT NULL COMMENT '수정자',
                                                          `version` INT NOT NULL DEFAULT 0 COMMENT '낙관적 락 버전',
                                                          `created_at` DATETIME NOT NULL COMMENT '생성일시 (yyyy-MM-dd HH:mm:ss)',
                                                          `created_by` VARCHAR(255) NOT NULL COMMENT '생성자',
                                                          PRIMARY KEY (`id`)
#                                                           UNIQUE KEY `UK_login_id` (`login_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci
    COMMENT = '회원 세션 정보';
