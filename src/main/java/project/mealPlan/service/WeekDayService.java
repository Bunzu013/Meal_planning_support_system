package project.mealPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.mealPlan.entity.WeekDay;
import project.mealPlan.repository.WeekDayRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeekDayService {
    @Autowired
    WeekDayRepository weekDayRepository;

    public ResponseEntity<?> getAllWeekDays()
    {
        try {
            List<Map<String,Object>> allWeekDaysList = new ArrayList<>();
            List<WeekDay> weekDays= weekDayRepository.findAll();
            for(WeekDay weekDay : weekDays)
            {
                Map<String,Object> weekDayInfo = new HashMap<>();
                weekDayInfo.put("weekDayName", weekDay.getWeekDayName());
                allWeekDaysList.add(weekDayInfo);
            }
            return new ResponseEntity<>(allWeekDaysList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
