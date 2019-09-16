package com.ydbus.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 说明：
 * Created by jjs on 2019/9/17.
 */
public class ExcelActivity extends AppCompatActivity {
    public void logg(String str) {
        LogUtils.e("ExcelActivity:   ", "" + str);
    }

    List<List<String>> values;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel);
        Intent intent = getIntent();
        if ("application/vnd.ms-excel".equals(intent.getType())) {
            //xlsx
            Uri fileUri = intent.getData();
            if (fileUri == null) {
                return;
            }
            file = UriUtils.uri2File(fileUri);
            PermissionUtils.permission(PermissionConstants.STORAGE)
                    .callback(new PermissionUtils.SimpleCallback() {
                        @Override
                        public void onGranted() {
                            logg("权限成功");
                            try {
                                fetchFile(file);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onDenied() {
                            logg("权限失败");
                        }
                    }).request();
        } else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(intent.getType())) {
            ToastUtils.showLong("请将xlsx文件转为xls格式再打开");
        } else {
            ToastUtils.showLong("只支持处理xls格式文件");
        }
    }

    private void fetchFile(File file) throws Exception {
        values = new ArrayList<>();
        HSSFWorkbook workbook = HSSFWorkbookFactory.createWorkbook(new POIFSFileSystem(new FileInputStream(file)));
        int sheetSize = workbook.getNumberOfSheets();
        logg("页数:" + sheetSize);
        HSSFSheet sheet = workbook.getSheetAt(3);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        logg("row:" + firstRowNum + "   " + lastRowNum);

        for (int i = 0; i < lastRowNum - firstRowNum; i++) {
            HSSFRow row = sheet.getRow(i + firstRowNum);
            if (row == null) {
                continue;
            }
            short firstCellNum = row.getFirstCellNum();
            short lastCellNum = row.getLastCellNum();
            // logg("cell:" + firstCellNum + "   " + lastCellNum);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < lastCellNum - firstCellNum; j++) {
                HSSFCell cell = row.getCell(j + firstCellNum);
                if (cell == null) {
                    continue;
                }
                if (cell.getCellType() == CellType.NUMERIC) {
                    sb.append(cell.getNumericCellValue()).append("|");
                } else if (cell.getCellType() == CellType.FORMULA) {
                    cell.setCellType(CellType.FORMULA);
                    sb.append(cell.getNumericCellValue()).append("|");
                } else {
                    sb.append(cell.toString()).append("|");
                }
            }
            logg(sb.toString());
        }
    }

/*    private void fetchFile(File file) throws Exception {
        values = new ArrayList<>();
        HSSFWorkbook workbook = HSSFWorkbookFactory.createWorkbook(new POIFSFileSystem(new FileInputStream(file)));
        int sheetSize = workbook.getNumberOfSheets();
        logg("页数:" + sheetSize);
        HSSFSheet sheet = workbook.getSheetAt(3);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        logg("row:" + firstRowNum + "   " + lastRowNum);

        for (int i = 0; i < lastRowNum - firstRowNum; i++) {
            HSSFRow row = sheet.getRow(i + firstRowNum);
            if (row == null) {
                continue;
            }
            short firstCellNum = row.getFirstCellNum();
            short lastCellNum = row.getLastCellNum();
            // logg("cell:" + firstCellNum + "   " + lastCellNum);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < lastCellNum - firstCellNum; j++) {
                HSSFCell cell = row.getCell(j + firstCellNum);
                if (cell == null) {
                    continue;
                }
                sb.append(cell.toString()).append("|");
            }
            logg(sb.toString());
        }
    }*/

      /* Workbook workbook = Workbook.getWorkbook(file);
        int numberOfSheets = workbook.getNumberOfSheets();//页数
        logg("页数:" + numberOfSheets);
        Sheet sheet = workbook.getSheet(0);//第一页
        //先行再列地读取
        // sheet.getRows()行数
        for (int i = 0; i < sheet.getColumns(); i++) {

            ArrayList<String> list = new ArrayList<>();
            for (int j = 0; j < sheet.getRows(); j++) {
                String info = sheet.getCell(i, j).getContents();
                //System.out.println(Cellinfo);
                list.add(info);
            }
            values.add(list);
        }

        for (int i = 0; i < values.size(); i++) {
            List<String> list = values.get(i);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < list.size(); j++) {
                sb.append(list.get(j)).append(" | ");
            }
            logg(sb.toString());
        }*/
}
