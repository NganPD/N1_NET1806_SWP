package online.be.service;

import online.be.entity.CourtSchedule;
import online.be.repository.CourtScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CourtScheduleService {
    @Autowired
    private CourtScheduleRepository courtScheduleRepository;

    public List<CourtSchedule> getAllCourtSchedules(){
        return courtScheduleRepository.findAll();
    }
}
