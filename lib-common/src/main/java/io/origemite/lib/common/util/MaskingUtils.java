package io.origemite.lib.common.util;

import java.util.regex.Pattern;

public class MaskingUtils {
    private MaskingUtils() {
        throw new AssertionError();
    }

    public static final String DASH = "-";
    public static final String DEFAULT_MASK_CHAR = "*";
    public static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("([\\d]{3,6}\\-[\\d]{2,6}\\-[\\d]{2,6})(\\-[\\d]{1,3})?");
    public static final int ACCOUNT_MASKING_COUNT = 5;

    /**
     *  전화번호 마스킹
     *
     *  assertEquals( "010-****-8870", JMaskingUtils.maskTel("010-7777-8870"));
     *  assertEquals( "010-****-8870", JMaskingUtils.maskTel("01077778870"));
     *  assertEquals( "02-***-2964",    JMaskingUtils.maskTel("02-341-2964"));
     *  assertEquals( "02-***-2964",    JMaskingUtils.maskTel("023412964"));
     *  assertEquals( "031-***-2964" , JMaskingUtils.maskTel("031-341-2964"));
     *  assertEquals( "031-***-2964" , JMaskingUtils.maskTel("0313412964"));
     *  assertEquals( "" , JMaskingUtils.maskTel(""));
     *  assertEquals( null , JMaskingUtils.maskTel(null));
     * @param str
     * @return
     */
    public static String maskTel(String str) {
        if (StringUtils.isEmpty(str))
            return str;

        if (str.contains(DASH)) {
            if (str.length() == 13) {
                return StringUtils.replaceGroup(str, "(\\d{3})-(\\d{4})-(\\d{4})",
                        StringUtils.concat("$1-", StringUtils.repeat(DEFAULT_MASK_CHAR, 4), "-$3"));
            } else if (str.length() == 12) {
                return StringUtils.replaceGroup(str, "(\\d{3})-(\\d{3})-(\\d{4})",
                        StringUtils.concat("$1-", StringUtils.repeat(DEFAULT_MASK_CHAR, 3), "-$3"));
            } else if (str.length() == 11) {
                return StringUtils.replaceGroup(str, "(\\d{2})-(\\d{3})-(\\d{4})",
                        StringUtils.concat("$1-", StringUtils.repeat(DEFAULT_MASK_CHAR, 3), "-$3"));
            }
        } else {
            if (str.length() == 11) {
                return StringUtils.replaceGroup(str, "(\\d{3})(\\d{4})(\\d{4})", StringUtils.concat("$1-", StringUtils.repeat(DEFAULT_MASK_CHAR, 4), "-$3"));
            } else if (str.length() == 10) {
                return StringUtils.replaceGroup(str, "(\\d{3})(\\d{3})(\\d{4})", StringUtils.concat("$1-", StringUtils.repeat(DEFAULT_MASK_CHAR, 3), "-$3"));
            } else if (str.length() == 9) {
                return StringUtils.replaceGroup(str, "(\\d{2})(\\d{3})(\\d{4})", StringUtils.concat("$1-", StringUtils.repeat(DEFAULT_MASK_CHAR, 3), "-$3"));
            }
        }

        return str;
    }

    /**
     * 아이디 마스킹
     * > 앞 두자리 제외하고 마스킹 처리
     * assertEquals( "gh*******", JMaskingUtils.maskId("ghkdwls30"));
     * @param str
     * @return
     */
    public static String maskId(String str) {
        if (StringUtils.isEmpty(str))
            return str;
        if (StringUtils.equalsLength(str, 2))
            return str;

        return StringUtils.replaceGroup(str, "(\\w{2})(\\w+)", StringUtils.concat("$1", StringUtils.repeat(DEFAULT_MASK_CHAR, str.length() - 2)));

    }

    /**
     * 법인등록번호 마스킹
     * assertEquals( "120111-*******", JMaskingUtils.maskRgsn("120111-0566317"));
     * assertEquals( "120111-*******", JMaskingUtils.maskRgsn("1201110566317"));
     * @param str
     * @return
     */
    public static String maskRgsn(String str) {
        return maskRsdn(str);
    }

    /**
     * 주민등록번호 마스킹
     * assertEquals( "890622-*******", JMaskingUtils.maskRsdn("890622-1111222"));
     * assertEquals( "890622-*******", JMaskingUtils.maskRsdn("8906221111222"));
     * @param str
     * @return
     */
    public static String maskRsdn(String str) {
        if (StringUtils.isEmpty(str))
            return str;

        if (str.matches("\\d{6}-\\d{7}")) {
            return StringUtils.replaceGroup(str, "(\\d{6})-(\\d{7})", StringUtils.concat("$1", "-", "*******"));
        } else if (str.matches("\\d{6}\\d{7}")) {
            return StringUtils.replaceGroup(str, "(\\d{6})(\\d{7})", StringUtils.concat("$1", "-", "*******"));
        }
        throw new IllegalArgumentException("The string in an unacceptable form.");
    }

    /**
     * 사업자번호 마스킹
     * assertEquals( "131-86-*****", JMaskingUtils.maskBsnn("131-86-27632"));
     * assertEquals( "131-86-*****", JMaskingUtils.maskBsnn("1318627632"));
     * @param str
     * @return
     */
    public static String maskBsnn(String str) {
        if (StringUtils.isEmpty(str))
            return str;

        if (str.matches("\\d{3}-\\d{2}-\\d{5}")) {
            return StringUtils.replaceGroup(str, "(\\d{3})-(\\d{2})-(\\d{5})", StringUtils.concat("$1", "-", "$2", "-", "*****"));
        } else if (str.matches("\\d{3}\\d{2}\\d{5}")) {
            return StringUtils.replaceGroup(str, "(\\d{3})(\\d{2})(\\d{5})", StringUtils.concat("$1", "-", "$2", "-", "*****"));
        }
        throw new IllegalArgumentException("The string in an unacceptable form.");
    }

    /**
     * 계좌번호 마스킹
     * assertEquals( "333-22-66****-*", JMaskingUtils.maskAcnn("333-22-666666-1"));
     * assertEquals( "333-333-6*****", JMaskingUtils.maskAcnn("333-333-666666"));
     * assertEquals( "4444-22-6*****", JMaskingUtils.maskAcnn("4444-22-666666"));
     * assertThrows(IllegalArgumentException.class, () -> JMaskingUtils.maskAcnn("aaaaa"));
     * assertThrows(IllegalArgumentException.class, () -> JMaskingUtils.maskAcnn("1-2-3-4-5"));
     * @param str
     * @return
     */
    public static String maskAcnn(String str) {

        if (!ACCOUNT_NUMBER_PATTERN.matcher(str).matches()) {
            throw new IllegalArgumentException("The string in an unacceptable form.");
        }

        return StringUtils.maskByRepeat(str, ACCOUNT_MASKING_COUNT, DEFAULT_MASK_CHAR.charAt(0), "-".toCharArray(), 0);
    }

    /**
     * 카드번호 마스킹
     * assertEquals( "1111-2222-****-4444", JMaskingUtils.maskCrdn("1111-2222-3333-4444"));
     * assertEquals( "1111-2222-****-4444", JMaskingUtils.maskCrdn("1111222233334444"));
     * @param str
     * @return
     */
    public static String maskCrdn(String str) {
        if (StringUtils.isEmpty(str))
            return str;

        if (str.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
            return StringUtils.replaceGroup(str, "(\\d{4})-(\\d{4})-(\\d{4})-(\\d{4})", StringUtils.concat("$1", "-", "$2", "-", "****", "-", "$4"));
        } else if (str.matches("\\d{4}\\d{4}\\d{4}\\d{4}")) {
            return StringUtils.replaceGroup(str, "(\\d{4})(\\d{4})(\\d{4})(\\d{4})", StringUtils.concat("$1", "-", "$2", "-", "****", "-", "$4"));
        }
        throw new IllegalArgumentException("The string in an unacceptable form.");
    }

    /**
     * 이메일 마스킹
     * assertThrows(IllegalArgumentException.class, () -> JMaskingUtils.maskEmail("aaaaa"));
     * assertThrows(IllegalArgumentException.class, () -> JMaskingUtils.maskEmail("aaaaa@"));
     * assertThrows(IllegalArgumentException.class, () -> JMaskingUtils.maskEmail("@111"));
     * assertEquals( "aa**@naver.com", JMaskingUtils.maskEmail("aaaa@naver.com"));
     * @param str
     * @return
     */
    public static String maskEmail(String str) {
        if (StringUtils.isEmpty(str))
            return str;
        if (!str.matches("[\\w.]+@\\w+\\.\\w+")) {
            //throw new IllegalArgumentException("The string in an unacceptable form.");
            return str;
        }
        return StringUtils.mask(str, str.indexOf("@") - 2, 2);
    }

    /**
     * 이름 마스킹
     *
     * assertEquals( "홍*동", JMaskingUtils.maskNm("홍길동"));
     * assertEquals( "가*****사", JMaskingUtils.maskNm("가나다라마바사"));
     * assertEquals( "강*", JMaskingUtils.maskNm("강풀"));
     * assertEquals( "강", JMaskingUtils.maskNm("강"));
     * assertEquals( null, JMaskingUtils.maskNm(null));
     * assertEquals( "", JMaskingUtils.maskNm(""));
     * @return
     */
    public static String maskName(String str) {
        if (StringUtils.isEmpty(str) || str.length() == 1)
            return str;

        if (str.length() == 2) {
            return StringUtils.concat(String.valueOf(str.charAt(0)), DEFAULT_MASK_CHAR);
        } else {
            return StringUtils.replaceGroup(str, "(.{1})(.+)(.{1})",
                    StringUtils.concat("$1", StringUtils.repeat(DEFAULT_MASK_CHAR, str.length() - 2), "$3"));
        }
    }
}
