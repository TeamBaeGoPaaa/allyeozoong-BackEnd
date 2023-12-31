package Hungry.PatientAnalystic.controller;

import Hungry.PatientAnalystic.dto.GroupAges;
import Hungry.PatientAnalystic.dto.PatientDto;
import Hungry.PatientAnalystic.dto.UserNameDto;
import Hungry.PatientAnalystic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class PatientController {
    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    private final PatientService patientService;

    @CrossOrigin
    @PostMapping("/getFrequencyAndAges")
    public ResponseEntity<Map<String, Object>> getFrequencyAndAges (@RequestParam("risk") int risk, @RequestParam("symptom") String symptom, @RequestParam("age") int age){

        System.out.println(symptom);
        System.out.println(age);

        PatientDto patientDTO = PatientDto.of("user", String.valueOf(age), symptom, risk); // DTO에 저장 (임시
        patientService.create(patientDTO.toEntity()); // db에 저장

        // 해당 질병의 발병 빈도(frequency)
        double percentage = patientService.getFrequency(symptom);

        // 연령층 별 발병자 수
        List<GroupAges> groupAges = patientService.getCountOfAgeGroup(symptom);

        // 응답 데이터를 준비
        Map<String, Object> response = new HashMap<>();

        response.put("Frequency", percentage);
        response.put("name","user");
        response.put("ages",groupAges);


        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/getUserData")
    public List<PatientDto> getUserData(@RequestBody UserNameDto userNameDto){
        List<PatientDto> patientDtoList = patientService.findAllByName(userNameDto.id());
        return patientDtoList;
    }

    @CrossOrigin
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "test";
    }
}
