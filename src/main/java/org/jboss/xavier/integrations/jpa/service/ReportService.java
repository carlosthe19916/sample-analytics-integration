package org.jboss.xavier.integrations.jpa.service;

import org.jboss.xavier.integrations.jpa.repository.ReportRepository;
import org.jboss.xavier.integrations.migrationanalytics.output.ReportDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportService
{
    @Autowired
    ReportRepository reportRepository;

    public Iterable<ReportDataModel> findReports()
    {
        return reportRepository.findAll();
    }
}
