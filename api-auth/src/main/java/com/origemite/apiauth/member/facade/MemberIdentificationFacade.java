package com.origemite.apiauth.member.facade;

import com.origemite.apiauth.auth.service.VaultService;
import com.origemite.lib.common.util.EncUtils;
import com.origemite.lib.common.util.MaskingUtils;
import com.origemite.lib.common.util.UuidUtils;
import com.origemite.lib.model.auth.dto.MemberIdentificationReq;
import com.origemite.lib.model.auth.dto.MemberIdentificationRes;
import com.origemite.lib.model.auth.service.MemberIdentificationService;
import com.origemite.lib.model.enums.auth.EnVaultType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.origemite.lib.common.util.ModelMapperUtil.map;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberIdentificationFacade {

    private final MemberIdentificationService memberIdentificationService;
    private final VaultService vaultService;

    public Page<MemberIdentificationRes.Item> search(MemberIdentificationReq.Filter filter, Pageable pageable) {
        return memberIdentificationService.search(filter, pageable);
    }

    @Transactional
    public MemberIdentificationRes.Id save(MemberIdentificationReq.Create create) {
        // 서비스 로직은 제외.
        create = setEncrypt(create);
        return memberIdentificationService.save(create);
    }

    @Transactional
    public MemberIdentificationRes.Id save(MemberIdentificationReq.CreateForEncrypt create) {
        MemberIdentificationReq.Create iCreate = map(create, MemberIdentificationReq.Create.class);
        return save(iCreate);
    }

    private String memberAesEncrypt(String s) {
        return vaultService.aesEncrypt(EnVaultType.MEMBER_AES, s);
    }

    private String memberAesDecrypt(String s) {
        return vaultService.aesDecrypt(EnVaultType.MEMBER_AES, s);
    }

    private MemberIdentificationReq.Create setEncrypt(MemberIdentificationReq.Create create) {
        create.setMemberId(UuidUtils.uuidToBase64(UUID.randomUUID()));
        create.setNameSha(EncUtils.encSHA512(create.getName()));
        create.setMobilePhoneNumberSha(EncUtils.encSHA512(create.getMobilePhoneNumber()));
        create.setEmailSha(EncUtils.encSHA512(create.getEmail()));
        create.setCiSha(EncUtils.encSHA512(create.getCi()));

        create.setName(memberAesEncrypt(create.getName()));
        create.setMobilePhoneNumber(memberAesEncrypt(create.getMobilePhoneNumber()));
        create.setEmail(memberAesEncrypt(create.getEmail()));
        create.setCi(memberAesEncrypt(create.getCi()));

        create.setCipherKeyId(EnVaultType.MEMBER_AES.getValue());

        return create;
    }

    private MemberIdentificationRes.Item setDecrypt(MemberIdentificationRes.Item item, boolean masking) {
        if (masking) {
            item.setName(MaskingUtils.maskName(memberAesDecrypt(item.getName())));
            item.setMobilePhoneNumber(MaskingUtils.maskTel(memberAesDecrypt(item.getMobilePhoneNumber())));
            item.setEmail(MaskingUtils.maskEmail(memberAesDecrypt(item.getEmail())));
            item.setCi(memberAesDecrypt(item.getCi()));
        } else {
            item.setName(memberAesDecrypt(item.getName()));
            item.setMobilePhoneNumber(memberAesDecrypt(item.getMobilePhoneNumber()));
            item.setEmail(memberAesDecrypt(item.getEmail()));
            item.setCi(memberAesDecrypt(item.getCi()));
        }

        return item;
    }


    public MemberIdentificationRes.Item findById(Integer id) {
        return memberIdentificationService.findById(id);
    }

    public MemberIdentificationRes.Item findByIdForDecrypt(Integer id) {
        MemberIdentificationRes.Item item = findById(id);
        item = setDecrypt(item, true);
        return item;
    }

    public MemberIdentificationRes.Item findByIdForDecryptForAdmin(Integer id) {
        MemberIdentificationRes.Item item = findById(id);
        item = setDecrypt(item, false);
        return item;
    }

    @Transactional
    public MemberIdentificationRes.Id saveById(Integer id, MemberIdentificationReq.Update update) {
        return memberIdentificationService.saveById(id, update);
    }

    @Transactional
    public void deleteById(Integer id) {
        memberIdentificationService.deleteById(id);
    }

    public List<MemberIdentificationRes.Item> findAll(MemberIdentificationReq.Filter filter) {
        return memberIdentificationService.findAll(filter);
    }

    public List<MemberIdentificationRes.Item> findByIds(List<Integer> ids) {
        return memberIdentificationService.findByIds(ids);
    }

    public Page<MemberIdentificationRes.Name> searchName(MemberIdentificationReq.Filter filter, Pageable pageable) {
        return memberIdentificationService.searchName(filter, pageable);
    }

    public List<MemberIdentificationRes.Name> findAllName(MemberIdentificationReq.Filter filter) {
        return memberIdentificationService.findAllName(filter);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberIdentificationService.deleteByIds(ids);
    }
}
