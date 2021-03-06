package com.example.project_1.Ke_Hoach_chi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_1.Adapter.ChiAdapter;
import com.example.project_1.Adapter.KhCHiAdapter;
import com.example.project_1.DAO.ChiDAO;
import com.example.project_1.DAO.KHchiDAO;
import com.example.project_1.DAO.NguoiDungDAO;
import com.example.project_1.Models.Chi;
import com.example.project_1.Models.KHchi;
import com.example.project_1.Models.NguoiDung;
import com.example.project_1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class KhChiFragment extends Fragment {

    private KHchiDAO kHchiDAO;
    private NguoiDungDAO nguoiDungDAO;
    private ListView lv_ds_ke_hoach_Chi;
    private List<KHchi> list_KhChi = new ArrayList<>();
    private FloatingActionButton fbtn_add_ke_hoach_Chi;
    private List<NguoiDung> list_ND = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    final Calendar calendar = Calendar.getInstance();
    private TextView tv_so_tien;
    private ChiDAO chiDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kh_chi, container, false);

        chiDAO = new ChiDAO( getActivity() );
        kHchiDAO = new KHchiDAO( getActivity() );
        nguoiDungDAO = new NguoiDungDAO( getActivity() );
        lv_ds_ke_hoach_Chi = view.findViewById(R.id.lv_ds_ke_hoach_Chi);
        fbtn_add_ke_hoach_Chi = view.findViewById(R.id.fbtn_add_ke_hoach_Chi);
        tv_so_tien = view.findViewById(R.id.tv_khCHi_so_tien);
        tv_so_tien.setText("T???ng ti???n d??? chi : " + kHchiDAO.get_GT("SELECT sum(soTienDuChi) FROM KeHoachChi;") );

        fbtn_add_ke_hoach_Chi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_Ke_hoach_chi();
            }
        });

        list_KhChi.clear();
        list_KhChi = kHchiDAO.getAll_Ke_hoach_chi();
        KhCHiAdapter adapter = new KhCHiAdapter( getActivity() , list_KhChi , tv_so_tien);
        lv_ds_ke_hoach_Chi.setAdapter(adapter);

        try {
            lv_ds_ke_hoach_Chi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    update_Ke_Hoach_Chi(position);
                }
            });
        } catch (Exception ex){
            Log.e("\t\tChi Fragment : Error\t" , ex.toString());
        }

        return view;
    }

    private void add_Ke_hoach_chi() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_tiet_kiem , null);

        TextInputLayout edt_ma_Du_Chi, edt_so_Tien_Du_Chi , edt_ngay_Du_Chi, edt_Chu_Thich;

        Button btn_huy = view.findViewById(R.id.btn_huy_tiet_Kiem);
        Button btn_add_ke_hoach_chi = view.findViewById(R.id.btn_add_Tiet_Kiem);
        ImageButton btn_ngay_nhan_tien = view.findViewById(R.id.btn_ngay_Tiet_Kiem);
        CheckBox cbk_Status = view.findViewById(R.id.ckb_tiet_Kiem);

        edt_ma_Du_Chi  = view.findViewById(R.id.edt_ma_Tiet_Kiem);
        Spinner spinner_userName = view.findViewById(R.id.spinner_userName_Tiet_kiem);
        edt_so_Tien_Du_Chi  = view.findViewById(R.id.edt_so_Tien_Tiet_KIem);
        edt_ngay_Du_Chi  = view.findViewById(R.id.edt_ngay_nhan_tien_Tiet_Kiem);
        edt_Chu_Thich  = view.findViewById(R.id.edt_Chu_Thich_Tiet_Kiem);
        spinner_userName.setSelection(0);
        get_nguoi_Dung(spinner_userName);
        edt_ma_Du_Chi.setHint("M?? D??? Chi");
        edt_so_Tien_Du_Chi.setHint("S??? Ti???n D??? Chi");
        edt_ngay_Du_Chi.setHint("Ng??y D??? Chi");
        cbk_Status.setText("Ch??a Chi");
        edt_ma_Du_Chi.setEnabled(false);
        set_Status(cbk_Status);
        edt_so_Tien_Du_Chi.setError("");


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder( getActivity() );
        builder.setView(view).setTitle("Th??m K??? Ho???ch Chi");
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        btn_ngay_nhan_tien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_time(edt_ngay_Du_Chi);
            }
        });

        btn_add_ke_hoach_chi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ma_Du_Chi , userName , so_Tien_Du_Chi , ngay_Du_Chi , chu_Thich;

                    ma_Du_Chi = edt_ma_Du_Chi.getEditText().getText().toString();
                    userName = spinner_userName.getSelectedItem().toString();
                    so_Tien_Du_Chi = edt_so_Tien_Du_Chi.getEditText().getText().toString();
                    ngay_Du_Chi = edt_ngay_Du_Chi.getEditText().getText().toString();
                    chu_Thich = edt_Chu_Thich.getEditText().getText().toString();
                    String regex_so = "[0-9]+";
                    String status = String.valueOf( cbk_Status.isChecked() );

                    if (  so_Tien_Du_Chi.isEmpty() ){

                        dialog_chung(0, getActivity(), "Ph???i nh???p S??? Ti???n D??? Chi");

                    } else if ( ! so_Tien_Du_Chi.matches(regex_so) ){

                        dialog_chung(0, getActivity(), "S??? ti???n ph???i nh???p d???ng S???");

                    }
                    else if (  ngay_Du_Chi.isEmpty() ){

                        dialog_chung(0, getActivity(), "Ph???i ch???n Ng??y D??? Chi");

                    } else if ( so_Tien_Du_Chi.length() > 10 ){

                        dialog_chung(0, getActivity(), "S??? ti???n ph???i < 1 t???");

                    } else if ( (Integer.parseInt(so_Tien_Du_Chi)) == 0
                            || (Integer.parseInt(so_Tien_Du_Chi)) < 0){
                        dialog_chung(0, getActivity(), "S??? Ti???n ph???i > 0");
                    } else {
                        KHchi kHchi = new KHchi(
                                "KHC_" + System.currentTimeMillis() ,
                                userName ,
                                so_Tien_Du_Chi ,
                                ngay_Du_Chi ,
                                chu_Thich ,
                                status
                        );

                        String[] get_tk = kHchi.getUserName().split(" | ");
                        String get_user = get_tk[0];

                        NguoiDung nd = list_ND.get( get_vi_tri(list_ND , get_user) );

                        Integer so_tien_CHi = Integer.parseInt( so_Tien_Du_Chi );
                        Integer tong_tien_TK = Integer.parseInt( nd.getTongSoTien() );

                        int so_tien =  tong_tien_TK - so_tien_CHi;

                        if ( so_tien_CHi > tong_tien_TK && (cbk_Status.isChecked()) == false){

//                            dialog_chung(0 , getActivity() , "S??? ti???n trong t??i kho???n c???a b???n ko ????? nha !!! ");
                            if ( kHchiDAO.inser_ke_hoach_chi( kHchi) > 0){
                                dialog_chung(1, getActivity(), "Th??m kho???n Chi Th??nh C??ng");
                                dialog.dismiss();

                                list_KhChi.clear();
                                list_KhChi = kHchiDAO.getAll_Ke_hoach_chi();
                                KhCHiAdapter adapter = new KhCHiAdapter( getActivity() , list_KhChi , tv_so_tien);
                                lv_ds_ke_hoach_Chi.setAdapter(adapter);

                                dialog.dismiss();
                            }
                        } else if ( so_tien_CHi > tong_tien_TK){

                            dialog_chung(0 , getActivity() , "S??? ti???n trong t??i kho???n c???a b???n ko ????? nha !!! ");

                        } else if ( so_tien_CHi == 0 ){

                            dialog_chung(0, getActivity(), "S??? ti???n ph???i > 0");

                        } else if ( kHchiDAO.inser_ke_hoach_chi( kHchi ) > 0) {

                            if ( (cbk_Status.isChecked()) == true){

                                nd.setTongSoTien(String.valueOf(so_tien));
                                nguoiDungDAO.updateNguoiDung(nd);
                                kHchi.setUserName(nd.toString());
                                //                        boolean kq = userName.equals( list_ND.get( get_vi_tri(list_ND , get_user) ).toString() );
//                        Log.e("\t\t" + userName , nd.toString()
//                                + " | " + String.valueOf( " " + kq +" -- ") + get_user + "\t");

                                if ( kHchiDAO.update_ke_hoach_chi( kHchi ) > 0 ){

                                }

                                if ( chiDAO.inser_Khoan_Chi( new Chi(
                                        "CT__" + System.currentTimeMillis() ,
                                        userName ,
                                        so_Tien_Du_Chi ,
                                        ngay_Du_Chi ,
                                        "K??? ho???ch ???? chi"
                                )) > 0 ){
                                    kHchiDAO.delete_Ke_hoach_Chi_By_ID( kHchi.getMaDuChi() );
                                }
                            }

                            dialog_chung(1, getActivity(), "Th??m K??? ho???ch Chi Th??nh C??ng");
                            dialog.dismiss();

                            list_KhChi.clear();
                            list_KhChi = kHchiDAO.getAll_Ke_hoach_chi();
                            KhCHiAdapter adapter = new KhCHiAdapter( getActivity() , list_KhChi , tv_so_tien);
                            lv_ds_ke_hoach_Chi.setAdapter(adapter);

                        } else if ( kHchiDAO.check_ID_KhChi( kHchi ) ) {

                            dialog_chung(0, getActivity(), "K??? Ho???ch Chi ???? t???n t???i !\n\nVui l??ng nh???p m?? kh??c.");

                        } else {

                            dialog_chung(1, getActivity(), "Th??m Th???t B???i");
                        }

                    }
                    tv_so_tien.setText("T???ng ti???n d??? chi : " + kHchiDAO.get_GT("SELECT sum(soTienDuChi) FROM KeHoachChi;") );
                } catch (Exception ex){
                    Log.e("Error add KH Chi\t\t" , ex.toString() );
                }
            }
        });

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                list_KhChi.clear();
                list_KhChi = kHchiDAO.getAll_Ke_hoach_chi();
                KhCHiAdapter adapter = new KhCHiAdapter( getActivity() , list_KhChi , tv_so_tien);
                lv_ds_ke_hoach_Chi.setAdapter(adapter);
            }
        });

        dialog.show();
    }

    private void dialog_chung(Integer so, Context context, String tb) {
        switch (so) {
            case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Th??ng B??o").setMessage(tb);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case 1:
                Toast.makeText(context, tb, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void get_nguoi_Dung(Spinner spinner) {
        list_ND = nguoiDungDAO.getAllNguoiDung();

        ArrayAdapter<NguoiDung> dataAdapter = new ArrayAdapter<NguoiDung>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                list_ND
        );

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void get_time(TextInputLayout edt){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity() ,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
                        edt.getEditText().setText(sdf.format(cal.getTime()));
                    }
                } , calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH) , calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }


    private void update_Ke_Hoach_Chi(Integer position) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_tiet_kiem , null);

        TextInputLayout edt_ma_Du_Chi, edt_so_Tien_Du_Chi, edt_ngay_Du_Chi, edt_Chu_Thich;

        Button btn_huy = view.findViewById(R.id.btn_huy_tiet_Kiem);
        Button btn_add_ke_hoach_chi = view.findViewById(R.id.btn_add_Tiet_Kiem);
        ImageButton btn_ngay_nhan_tien = view.findViewById(R.id.btn_ngay_Tiet_Kiem);
        CheckBox cbk_Status = view.findViewById(R.id.ckb_tiet_Kiem);

        edt_ma_Du_Chi  = view.findViewById(R.id.edt_ma_Tiet_Kiem);
        Spinner spinner_userName = view.findViewById(R.id.spinner_userName_Tiet_kiem);
        edt_so_Tien_Du_Chi  = view.findViewById(R.id.edt_so_Tien_Tiet_KIem);
        edt_ngay_Du_Chi  = view.findViewById(R.id.edt_ngay_nhan_tien_Tiet_Kiem);
        edt_Chu_Thich  = view.findViewById(R.id.edt_Chu_Thich_Tiet_Kiem);
        spinner_userName.setSelection(0);
        get_nguoi_Dung(spinner_userName);
        edt_ma_Du_Chi.setHint("M?? D??? Chi");
        edt_so_Tien_Du_Chi.setHint("S??? Ti???n D??? Chi");
        edt_ngay_Du_Chi.setHint("Ng??y D??? Chi");
        cbk_Status.setText("Ch??a Chi");
        edt_ma_Du_Chi.setEnabled(false);
        set_Status(cbk_Status);

        list_KhChi.clear();
        list_KhChi = kHchiDAO.getAll_Ke_hoach_chi();
        KhCHiAdapter adapter = new KhCHiAdapter(getActivity() , list_KhChi , tv_so_tien);
        lv_ds_ke_hoach_Chi.setAdapter(adapter);
        edt_ma_Du_Chi.getEditText().setText( list_KhChi.get(position).getMaDuChi() );
        edt_ma_Du_Chi.getEditText().setEnabled(false);
        spinner_userName.setSelection( getIndex( spinner_userName , list_KhChi.get(position).getUserName() ) );
        edt_so_Tien_Du_Chi.getEditText().setText( list_KhChi.get(position).getSoTienDuChi() );
        edt_ngay_Du_Chi.getEditText().setText( list_KhChi.get(position).getNgayDuChi() );
        edt_Chu_Thich.getEditText().setText( list_KhChi.get(position).getChuThich() );

        if ( Boolean.parseBoolean( list_KhChi.get(position).getStatus() ) ){
            cbk_Status.setChecked( Boolean.parseBoolean( list_KhChi.get(position).getStatus() ) );
            cbk_Status.setText("???? Chi");
        } else {
            cbk_Status.setChecked( Boolean.parseBoolean( list_KhChi.get(position).getStatus() ) );
            cbk_Status.setText("Ch??a Chi");
        }
        btn_add_ke_hoach_chi.setText("C???p Nh???t");

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setView(view).setTitle("C???p Nh???t K??? Ho???ch Chi");
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        btn_ngay_nhan_tien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_time(edt_ngay_Du_Chi);
            }
        });

        btn_add_ke_hoach_chi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ma_Du_Chi , userName , so_Tien_Du_Chi , ngay_Du_Chi , chu_Thich;

                    ma_Du_Chi = edt_ma_Du_Chi.getEditText().getText().toString();
                    userName = spinner_userName.getSelectedItem().toString();
                    so_Tien_Du_Chi = edt_so_Tien_Du_Chi.getEditText().getText().toString();
                    ngay_Du_Chi = edt_ngay_Du_Chi.getEditText().getText().toString();
                    chu_Thich = edt_Chu_Thich.getEditText().getText().toString();
                    String regex_so = "[0-9]+";
                    String status = String.valueOf( cbk_Status.isChecked() );

                    if (  so_Tien_Du_Chi.isEmpty() ){

                        dialog_chung(0, getActivity(), "Ph???i nh???p S??? Ti???n D??? Chi");

                    } else if ( ! so_Tien_Du_Chi.matches(regex_so) ){

                        dialog_chung(0, getActivity(), "S??? ti???n ph???i nh???p d???ng S???");

                    }
                    else if (  ngay_Du_Chi.isEmpty() ){

                        dialog_chung(0, getActivity(), "Ph???i ch???n Ng??y D??? Chi");

                    } else if ( so_Tien_Du_Chi.length() > 10 ){

                        dialog_chung(0, getActivity(), "S??? ti???n ph???i < 1 t???");

                    } else if ( (Integer.parseInt(so_Tien_Du_Chi)) == 0
                            || (Integer.parseInt(so_Tien_Du_Chi)) < 0){
                        dialog_chung(0, getActivity(), "S??? Ti???n ph???i > 0");
                    } else {
                        KHchi kHchi = new KHchi(
                                ma_Du_Chi ,
                                userName ,
                                so_Tien_Du_Chi ,
                                ngay_Du_Chi ,
                                chu_Thich ,
                                status
                        );

                        String[] get_tk = kHchi.getUserName().split(" | ");
                        String get_user = get_tk[0];

                        NguoiDung nd = list_ND.get( get_vi_tri(list_ND , get_user) );

                        Integer so_tien_CHi = Integer.parseInt( so_Tien_Du_Chi );
                        Integer tong_tien_TK = Integer.parseInt( nd.getTongSoTien() );

                        int so_tien =  tong_tien_TK - so_tien_CHi;

                        if ( so_tien_CHi > tong_tien_TK && (cbk_Status.isChecked()) == false){

//                            dialog_chung(0 , getActivity() , "S??? ti???n trong t??i kho???n c???a b???n ko ????? nha !!! ");
                            if ( kHchiDAO.update_ke_hoach_chi( kHchi) > 0){
                                dialog_chung(1, getActivity(), "C???p nh???t k??? ho???ch Chi Th??nh C??ng");
                                dialog.dismiss();

                                list_KhChi.clear();
                                list_KhChi = kHchiDAO.getAll_Ke_hoach_chi();
                                KhCHiAdapter adapter = new KhCHiAdapter( getActivity() , list_KhChi , tv_so_tien);
                                lv_ds_ke_hoach_Chi.setAdapter(adapter);

                                dialog.dismiss();
                            }
                        } else if ( so_tien_CHi > tong_tien_TK){

                            dialog_chung(0 , getActivity() , "S??? ti???n trong t??i kho???n c???a b???n ko ????? nha !!! ");

                        } else if ( so_tien_CHi == 0 ){

                            dialog_chung(0, getActivity(), "S??? ti???n ph???i > 0");

                        } else if ( kHchiDAO.update_ke_hoach_chi( kHchi ) > 0) {

                            if ( (cbk_Status.isChecked()) == true){

                                nd.setTongSoTien(String.valueOf(so_tien));
                                nguoiDungDAO.updateNguoiDung(nd);
                                kHchi.setUserName(nd.toString());
                                //                        boolean kq = userName.equals( list_ND.get( get_vi_tri(list_ND , get_user) ).toString() );
//                        Log.e("\t\t" + userName , nd.toString()
//                                + " | " + String.valueOf( " " + kq +" -- ") + get_user + "\t");

                                if ( kHchiDAO.update_ke_hoach_chi( kHchi ) > 0 ){

                                }

                                if ( chiDAO.inser_Khoan_Chi( new Chi(
                                        "CT__" + System.currentTimeMillis() ,
                                        userName ,
                                        so_Tien_Du_Chi ,
                                        ngay_Du_Chi ,
                                        "K??? ho???ch ???? chi"
                                )) > 0 ){
                                    kHchiDAO.delete_Ke_hoach_Chi_By_ID( kHchi.getMaDuChi() );
                                }
                            }

                            dialog_chung(1, getActivity(), "C???p Nh???t K??? ho???ch Chi Th??nh C??ng");
                            dialog.dismiss();

                            list_KhChi.clear();
                            list_KhChi = kHchiDAO.getAll_Ke_hoach_chi();
                            KhCHiAdapter adapter = new KhCHiAdapter( getActivity() , list_KhChi , tv_so_tien);
                            lv_ds_ke_hoach_Chi.setAdapter(adapter);

                        } else {

                            dialog_chung(1, getActivity(), "C???p nh???t Th???t B???i");
                        }

                    }
                    tv_so_tien.setText("T???ng ti???n d??? chi : " + kHchiDAO.get_GT("SELECT sum(soTienDuChi) FROM KeHoachChi;") );
                } catch (Exception ex){
                    Log.e("Error add KH Chi\t\t" , ex.toString() );
                }
            }
        });

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                list_KhChi.clear();
                list_KhChi = kHchiDAO.getAll_Ke_hoach_chi();
                KhCHiAdapter adapter = new KhCHiAdapter( getActivity() , list_KhChi , tv_so_tien);
                lv_ds_ke_hoach_Chi.setAdapter(adapter);
            }
        });

        dialog.show();
    }

    private Integer get_vi_tri(List<NguoiDung> nd , String user){
        for (int i = 0; i < nd.size() ; i++) {
            if ( nd.get(i).getUserName().equals( user ) ){
                return i;
            }
        }
        return 0;
    }

    private void set_Status(CheckBox cbk){
        cbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( cbk.isChecked() ){
                    cbk.setText("???? Chi");
                } else {
                    cbk.setText("Ch??a Chi");
                }
            }
        });
    }
}