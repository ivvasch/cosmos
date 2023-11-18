package ru.cosmotask.service;

import ru.cosmotask.dto.CompletedProcedureDTO;
import ru.cosmotask.dto.ProcedureDTO;
import ru.cosmotask.model.CompletedProcedure;
import ru.cosmotask.model.Procedure;

import java.util.List;

public interface ProcedureService {
    String saveProcedure(ProcedureDTO procedureDTO);
    String saveCompletedProcedure(CompletedProcedureDTO procedureDTO);

    String deleteProcedure(String id);

    String deleteCompletedProcedure(String id);

    List<CompletedProcedure> getAllCompletedProcedureByPatientId(String patientId);

    Procedure getProcedureById(String id);

    CompletedProcedure getCompletedProcedureById(String id);

    String updateCompletedProcedure(CompletedProcedure completedProcedureDTO);

    String checkCompletedProcedureForDelete(String id);
}
