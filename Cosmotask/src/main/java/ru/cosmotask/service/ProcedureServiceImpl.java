package ru.cosmotask.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cosmotask.dto.CompletedProcedureDTO;
import ru.cosmotask.dto.ProcedureDTO;
import ru.cosmotask.model.Comment;
import ru.cosmotask.model.CompletedProcedure;
import ru.cosmotask.model.Patient;
import ru.cosmotask.model.Procedure;
import ru.cosmotask.repository.CommentRepository;
import ru.cosmotask.repository.CompletedProcedureRepository;
import ru.cosmotask.repository.PatientRepository;
import ru.cosmotask.repository.ProcedureRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProcedureServiceImpl implements ProcedureService {

    private final ProcedureRepository procedureRepository;
    private final CompletedProcedureRepository completedProcedureRepository;
    private final PatientRepository patientRepository;
    private final CommentRepository commentRepository;


    public ProcedureServiceImpl(ProcedureRepository procedureRepository, CompletedProcedureRepository completedProcedureRepository, PatientRepository patientRepository, CommentRepository commentRepository) {
        this.procedureRepository = procedureRepository;
        this.completedProcedureRepository = completedProcedureRepository;
        this.patientRepository = patientRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public String saveProcedure(ProcedureDTO procedureDTO) {
        Procedure procedure = procedureDTO.convertToProcedure();
        Procedure saved = procedureRepository.save(procedure);
        return saved.getId();
    }

    @Override
    public String deleteProcedure(String id) {
        List<CompletedProcedure> allByProcedureId = completedProcedureRepository.findAllByProcedureId(id);
        if (allByProcedureId.isEmpty()) {
            procedureRepository.deleteById(id);
            return id;
        }
        return null;
    }

    @Override
    public Procedure getProcedureById(String id) {
        Optional<Procedure> procedureById = procedureRepository.findById(id);
        return procedureById.orElse(null);
    }

    // ---------------------------

    @Override
    public CompletedProcedure getCompletedProcedureById(String id) {
        Optional<CompletedProcedure> completedProcedureById = completedProcedureRepository.findById(id);
        return completedProcedureById.orElse(null);
    }

    @Override
    public String updateCompletedProcedure(CompletedProcedure completedProcedure) {
        CompletedProcedure saved = completedProcedureRepository.save(completedProcedure);
        return saved.getId();
    }

    @Override
    @Transactional
    public String saveCompletedProcedure(CompletedProcedureDTO procedureDTO) {
        CompletedProcedure completedProcedure = dtoToCompletedProcedure(procedureDTO);
        if (completedProcedure == null) {
            return null;
        }
        Comment comment = completedProcedure.getComments().get(0);
        commentRepository.save(comment);
        CompletedProcedure saved = completedProcedureRepository.save(completedProcedure);
        return saved.getId();
    }

    @Override
    public String checkCompletedProcedureForDelete(String id) {
        String result = String.format("CompletedProcedure with id %s will be permanently deleted. Are you sure?", id);
        if (completedProcedureRepository.findById(id).orElse(null) != null) {
            return result;
        }
        return "Completed procedure wasn't found";
    }

    @Override
    public String deleteCompletedProcedure(String id) {
        String result = null;
        if (completedProcedureRepository.findById(id).orElse(null) != null) {
            completedProcedureRepository.deleteById(id);
        }
        CompletedProcedure completedProcedure = completedProcedureRepository.findById(id).orElse(null);
        if (completedProcedure == null) {
            result = "Completed procedure was delete successfully";
        }
        return result;
    }

    @Override
    public List<CompletedProcedure> getAllCompletedProcedureByPatientId(String patientId) {
        return completedProcedureRepository.findAllByPatientId(patientId);
    }

    private CompletedProcedure dtoToCompletedProcedure(CompletedProcedureDTO procedureDTO) {
        Patient patient = patientRepository.findById(procedureDTO.getPatientId()).orElse(null);
        Procedure procedure = procedureRepository.findById(procedureDTO.getProcedureId()).orElse(null);
        Comment comment;
        List<Comment> comments = new ArrayList<>();
        String dtoComment = procedureDTO.getComment();
        if (!StringUtils.isBlank(dtoComment) && procedure != null) {
            comment = new Comment();
            comment.setText(dtoComment);
            comments.add(comment);
        }
        if (patient == null || procedure == null) {
            return null;
        }
        return CompletedProcedure.builder()
                .id(procedureDTO.getProcedureId())
                .patient(patient)
                .procedure(procedure)
                .cost(procedureDTO.getCost())
                .date(LocalDate.now())
                .nextDate(procedureDTO.getNextDate())
                .necessaryNextProcedure(procedureDTO.isNecessaryNextProcedure())
                .comments(comments)
                .done(procedureDTO.isDone())
                .build();
    }
}
