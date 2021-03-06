package controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import DAO.sprFirmDAO;
import DAO.sprIncidentStatusDAO;
import DAO.sprServiceDAO;
import DAO.tIncidentDAO;
import beans.sprFirm;
import beans.sprIncidentStatus;
import beans.sprService;
import beans.tIncident;
import beans_JPA.TIncident;
import beans_JPA.TSprUsers;
import interfaces.controllerInterface;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import static util.utils.NOW_LOCAL_DATE;
import util.utils.btnStatus;
import static util.utils.getLocalDate;

/**
 * FXML Controller class
 *
 * @author vasil
 */
public class UpdIncidentController implements Initializable, controllerInterface {

    /**
     * Initializes the controller class.
     */
    private final Logger log = Logger.getLogger(UpdIncidentController.class);
    private TIncident incident = null;
    private Stage dialogStage;
    private TSprUsers currentUser;
    private DataSource dataSource;    
    private btnStatus formResult = btnStatus.btnCancel;
    private EntityManager em;

    @FXML
    DatePicker idDPFDate;

    @FXML
    ComboBox idCBFirm;

    @FXML
    ComboBox idCBService;

    @FXML
    TextField idTFComment;

    @FXML
    DatePicker idDPDateCreated;

    @FXML
    TextField idTFUser;

    @FXML
    Button idBtnSave;

    @FXML
    Button idBtnCancel;

    @FXML
    ComboBox idCBIncidentStatus;

    @FXML
    public void btnUpdClick(ActionEvent actionEvent) {
        // Нажатие на кнопку сохранить
        log.info("btnUpdClick -> " + actionEvent);
        tIncident item = new tIncident();
        item.setFComment(idTFComment.getText());

        LocalDate localDate = idDPFDate.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        item.setFDate(date);
        item.setFFirmId(((sprFirm) idCBFirm.getSelectionModel().getSelectedItem()).getId());
        item.setFServiceId(((sprService) idCBService.getSelectionModel().getSelectedItem()).getId());
        item.setFComment(idTFComment.getText());
        item.setFDateCreated(date);
        item.setFIncidentStatusId(((sprIncidentStatus) idCBIncidentStatus.getValue()).getId());
        item.setId(incident.getId());
        item.setFUserId(incident.getFUserId().getId());
        log.info(item.toString());
        (new tIncidentDAO(dataSource)).updateItem(item);
        this.formResult = btnStatus.btnOK;
        dialogStage.close();
    }

    private ObservableList<sprFirm> getFirmList() {
        ObservableList<sprFirm> firmList = FXCollections.observableArrayList();
        List<sprFirm> tList = (new sprFirmDAO(dataSource).getItemList());
        tList.forEach((item) -> {
            firmList.add(item);
        });
        return firmList;
    }

    private ObservableList<sprService> getServiceList() {
        ObservableList<sprService> serviceList = FXCollections.observableArrayList();
        List<sprService> tList = (new sprServiceDAO(dataSource).getItemList());
        tList.forEach((item) -> {
            serviceList.add(item);
        });
        return serviceList;
    }

    private ObservableList<sprIncidentStatus> getStatusList() {
        ObservableList<sprIncidentStatus> statusList = FXCollections.observableArrayList();
        List<sprIncidentStatus> tList = (new sprIncidentStatusDAO(dataSource).getItemList());
        tList.forEach((item) -> {
            statusList.add(item);
        });
        return statusList;
    }

    public void initFormField(TIncident inc) {
        this.incident = inc;
        log.info("initFormField -> " + inc.toString());
        idTFComment.setText(inc.getFComment());
        idCBFirm.setValue(new sprFirm(inc.getFFirmId().getId(), inc.getFFirmId().getFName()));
        idCBService.setValue(new sprService(inc.getFServiceId().getId(), inc.getFServiceId().getFName()));
        idDPDateCreated.setValue(getLocalDate(inc.getFDateCreated()));
        idTFUser.setText(inc.getFUserId().getFName());
        idDPFDate.setValue(getLocalDate(inc.getFDate()));
        idCBIncidentStatus.setValue(new sprIncidentStatus(inc.getFIncidentStatusId().getId(), inc.getFIncidentStatusId().getFName()));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    public TSprUsers getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @Override
    public void setCurrentUser(TSprUsers currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void initForm() {
        try {
            log.info("initialize -> ");
            idDPFDate.setValue(NOW_LOCAL_DATE());
            idCBFirm.getItems().addAll(getFirmList());
            idCBService.getItems().addAll(getServiceList());
            idDPDateCreated.setValue(NOW_LOCAL_DATE());
            idTFUser.setText("Test");
            idCBIncidentStatus.getItems().addAll(getStatusList());
        } catch (Exception e) {
            log.error(e);
        }
    }

//    @Override
//    public formLocator showDialog(DataSource dataSource, Stage stage, Parent root, String windowCaption) {
//        try (formLocator res = new formLocator()) {
//            
//            
//            
//            return res;
//        } catch (Exception ex) {
//            log.error(ex);
//        }
//        return null;
//    }

    public btnStatus getFormResult() {
        return formResult;
    }

    @Override
    public void setEM(EntityManager em) {
        this.em = em;
    }

}
