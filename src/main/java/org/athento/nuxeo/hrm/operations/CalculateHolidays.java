package org.athento.nuxeo.hrm.operations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.athento.nuxeo.operations.AthentoDocumentQueryOperation;
import org.athento.nuxeo.operations.security.AbstractAthentoOperation;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Operation(id = CalculateHolidays.ID, category = "Athento", label = "Calculate holidays depending on starting date.", description = "Returns the number of holidays assigned to an user.")
public class CalculateHolidays extends AbstractAthentoOperation {

    private static final Log _log = LogFactory
            .getLog(AthentoDocumentQueryOperation.class);

    public final static String ID = "Athento.CalculateHolidays";

    private final static float daysInAyear = 365;
    private final static float holidays = 30;

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "incorporationDate")
    protected Date incorporationDate;

    @Param(name = "anualityStartDate")
    protected Date anualityStartDate;

    @Param(name = "output")
    protected String outputName;


    @OperationMethod
    public void run(){
        calculateHolidays(ctx, incorporationDate, anualityStartDate, outputName);
    }

    private void calculateHolidays(OperationContext ctx, Date incorporationDate, Date anualityStartDate, String outputName) {
        LocalDate incorporationLocalDate = incorporationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate anualityStartLocalDate = anualityStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(incorporationLocalDate, anualityStartLocalDate);
        _log.debug("Days between " + incorporationLocalDate + " and " + anualityStartDate + " are " + daysBetween);
        float ratio = holidays / daysInAyear;
        _log.debug("Ratio is --> " + ratio);
        _log.debug("Ratio * daysBetween --> " + (ratio * daysBetween));
        double daysOfHolidays = (int) Math.floor(ratio * daysBetween);
        _log.debug("Days of holidays --> " + daysOfHolidays);
        ctx.put(outputName,daysOfHolidays);
    }

}