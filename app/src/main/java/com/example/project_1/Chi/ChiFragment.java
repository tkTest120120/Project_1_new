package com.example.project_1.Chi;

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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project_1.Adapter.ChiAdapter;
import com.example.project_1.Adapter.ThuAdapter;
import com.example.project_1.DAO.ChiDAO;
import com.example.project_1.DAO.NguoiDungDAO;
import com.example.project_1.Models.Chi;
import com.example.project_1.Models.NguoiDung;
import com.example.project_1.Models.Thu;
import com.example.project_1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ChiFragment extends Fragment {

    private ChiDAO chiDAO;
    private NguoiDungDAO nguoiDungDAO;
    private ListView lv_ds_Chi;
    private List<Chi> list_CHi = new ArrayList<>();
    private FloatingActionButton fbtn_add_khoan_Chi;
    private List<NguoiDung> list_ND = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    final Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chi, container, false);

        chiDAO = new ChiDAO( getActivity() );
        nguoiDungDAO = new NguoiDungDAO( getActivity() );
        lv_ds_Chi = view.findViewById(R.id.lv_ds_Chi);
        fbtn_add_khoan_Chi = view.findViewById(R.id.fbtn_add_khoan_Chi);

        fbtn_add_khoan_Chi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_Khoan_Chi();
            }
        });

        list_CHi.clear();
        list_CHi = chiDAO.getAll_Khoan_Chi() ;
        ChiAdapter adapter = new ChiAdapter(getActivity() , list_CHi);
        lv_ds_Chi.setAdapter(adapter);

        try {
            lv_ds_Chi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    update_Khoan_Chi(position);
                }
            });
        } catch (Exception ex){
            Log.e("\t\tChi Fragment : Error\t" , ex.toString());
        }

        return view;
    }

    private void add_Khoan_Chi() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_khoan_thu , null);

        TextInputLayout edt_ma_chi_tieu, edt_so_tien_chi , edt_ngay_Chi, edt_Chu_Thich;

        Button btn_huy = view.findViewById(R.id.btn_huy);
        Button btn_add_Khoan_Chi = view.findViewById(R.id.btn_add_Khoan_Thu);
        ImageButton btn_ngay_nhan_tien = view.findViewById(R.id.btn_ngay_nhan_tien);
        edt_ma_chi_tieu  = view.findViewById(R.id.edt_ma_thu_nhap);
        Spinner spinner_userName = view.findViewById(R.id.spinner_userName);
        edt_so_tien_chi  = view.findViewById(R.id.edt_so_Tien_THu);
        edt_ngay_Chi  = view.findViewById(R.id.edt_ngay_nhan_tien);
        edt_Chu_Thich  = view.findViewById(R.id.edt_Chu_Thich_khoan_thu);
        spinner_userName.setSelection(0);
        get_nguoi_Dung(spinner_userName);
        edt_ma_chi_tieu.setHint("M?? Chi Ti??u");
        edt_so_tien_chi.setHint("S??? Ti???n Chi");
        edt_ngay_Chi.setHint("Ng??y Chi");
        edt_ma_chi_tieu.setEnabled(false);


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder( getActivity() );
        builder.setView(view).setTitle("Th??m Kho???n Chi");
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        btn_ngay_nhan_tien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_time(edt_ngay_Chi);
            }
        });

        btn_add_Khoan_Chi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma_chi_tieu , userName , so_tien_chi , ngay_Chi , chu_Thich;
                ma_chi_tieu = edt_ma_chi_tieu.getEditText().getText().toString();
                userName = spinner_userName.getSelectedItem().toString();
                so_tien_chi = edt_so_tien_chi.getEditText().getText().toString();
                ngay_Chi = edt_ngay_Chi.getEditText().getText().toString();
                chu_Thich = edt_Chu_Thich.getEditText().getText().toString();
                String regex_so = "[0-9]+";

                if (  so_tien_chi.isEmpty() ){

                    dialog_chung(0, getActivity(), "Ph???i nh???p S??? Ti???n Chi");

                } else if ( ! so_tien_chi.matches(regex_so) ){

                    dialog_chung(0, getActivity(), "S??? ti???n ph???i nh???p d???ng S???");

                }
                else if (  ngay_Chi.isEmpty() ){

                    dialog_chung(0, getActivity(), "Ph???i ch???n Ng??y Chi");

                } else if ( so_tien_chi.length() > 10){

                    dialog_chung(0, getActivity(), "S??? ti???n ph???i < 1 t???");

                }  else {
                    try {
                        Chi chi = new Chi(
                                "CT_" + System.currentTimeMillis() ,
                                userName ,
                                so_tien_chi ,
                                ngay_Chi ,
                                chu_Thich
                        );

                        String[] get_tk = chi.getUserName().split(" | ");
                        String get_user = get_tk[0];

                        NguoiDung nd = list_ND.get( get_vi_tri(list_ND , get_user) );

                        Integer so_tien_CHi = Integer.parseInt( so_tien_chi );
                        Integer tong_tien_TK = Integer.parseInt( nd.getTongSoTien() );

                        int so_tien =  tong_tien_TK - so_tien_CHi;

//                        boolean kq = so_tien_CHi > tong_tien_TK;
//                        Log.e("----------------------\t" , "\t\t\t" + so_tien +
//                                " " + tong_tien_TK + " " + so_tien_CHi + " |" + kq);
                        if ( so_tien_CHi > tong_tien_TK ){

                            dialog_chung(0, getActivity(), "B???n Kh??ng ????? ti???n chi tr???");

                        } else if ( so_tien_CHi == 0
                                    || so_tien < 0){

                            dialog_chung(0, getActivity(), "S??? ti???n chi ph???i > 0");

                        } else if ( chiDAO.inser_Khoan_Chi( chi ) > 0) {

                            nd.setTongSoTien(String.valueOf(so_tien));
                            nguoiDungDAO.updateNguoiDung(nd);
                            chi.setUserName(nd.toString());
                            //                        boolean kq = userName.equals( list_ND.get( get_vi_tri(list_ND , get_user) ).toString() );
//                        Log.e("\t\t" + userName , nd.toString()
//                                + " | " + String.valueOf( " " + kq +" -- ") + get_user + "\t");

                            if ( chiDAO.update_Khoan_Chi( chi ) > 0 ){

                            }

                            dialog_chung(1, getActivity(), "Th??m kho???n Chi Th??nh C??ng");
                            dialog.dismiss();

                            list_CHi.clear();
                            list_CHi = chiDAO.getAll_Khoan_Chi() ;
                            ChiAdapter adapter = new ChiAdapter( getActivity() , list_CHi );
                            lv_ds_Chi.setAdapter(adapter);

                        } else if ( chiDAO.check(chi) ) {

                            dialog_chung(0, getActivity(), "Kho???n Chi ???? t???n t???i !\n\nVui l??ng nh???p m?? kh??c.");

                        } else {

                            dialog_chung(1, getActivity(), "Th??m Th???t B???i");
                        }

                    } catch (Exception ex) {
                        Log.e("Error ????ng K??  : \t\t", ex.toString());
                    }
                }

            }
        });

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                list_CHi.clear();
                list_CHi = chiDAO.getAll_Khoan_Chi() ;
                ChiAdapter adapter = new ChiAdapter( getActivity() , list_CHi );
                lv_ds_Chi.setAdapter(adapter);
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

    private void update_Khoan_Chi(Integer position){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_khoan_thu , null);

        TextInputLayout edt_ma_chi_tieu, edt_so_tien_chi, edt_ngay_Chi, edt_Chu_Thich;

        Button btn_huy = view.findViewById(R.id.btn_huy);
        Button btn_add_Khoan_Chi = view.findViewById(R.id.btn_add_Khoan_Thu);
        ImageButton btn_ngay_nhan_tien = view.findViewById(R.id.btn_ngay_nhan_tien);
        edt_ma_chi_tieu  = view.findViewById(R.id.edt_ma_thu_nhap);
        Spinner spinner_userName = view.findViewById(R.id.spinner_userName);
        edt_so_tien_chi  = view.findViewById(R.id.edt_so_Tien_THu);
        edt_ngay_Chi  = view.findViewById(R.id.edt_ngay_nhan_tien);
        edt_Chu_Thich  = view.findViewById(R.id.edt_Chu_Thich_khoan_thu);
        spinner_userName.setSelection(0);
        get_nguoi_Dung(spinner_userName);
        edt_ma_chi_tieu.setHint("M?? Chi Ti??u");
        edt_so_tien_chi.setHint("S??? Ti???n Chi");
        edt_ngay_Chi.setHint("Ng??y Chi");
        edt_so_tien_chi.getEditText().setEnabled(false);

        list_CHi.clear();
        list_CHi = chiDAO.getAll_Khoan_Chi() ;
        edt_ma_chi_tieu.getEditText().setText( list_CHi.get(position).getMaChiTieu() );
        edt_ma_chi_tieu.getEditText().setEnabled(false);
        spinner_userName.setSelection( getIndex( spinner_userName , list_CHi.get(position).getUserName() ) );
        edt_so_tien_chi.getEditText().setText( list_CHi.get(position).getSoTienChi() );
        edt_ngay_Chi.getEditText().setText( list_CHi.get(position).getNgayChi() );
        edt_Chu_Thich.getEditText().setText( list_CHi.get(position).getChuThich() );
        btn_add_Khoan_Chi.setText("C???p Nh???t");

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setView(view).setTitle("C???p Nh???t Kho???n Chi");
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        btn_ngay_nhan_tien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_time(edt_ngay_Chi);
            }
        });

        btn_add_Khoan_Chi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma_chi_tieu , userName , so_tien_chi , ngay_chi , chu_Thich;
                ma_chi_tieu = edt_ma_chi_tieu.getEditText().getText().toString();
                userName = spinner_userName.getSelectedItem().toString();
                so_tien_chi = edt_so_tien_chi.getEditText().getText().toString();
                ngay_chi = edt_ngay_Chi.getEditText().getText().toString();
                chu_Thich = edt_Chu_Thich.getEditText().getText().toString();
                String regex_so = "[0-9]+";

                if (  so_tien_chi.isEmpty() ){

                    dialog_chung(0, getActivity(), "Ph???i nh???p S??? Ti???n");

                } else if ( ! so_tien_chi.matches(regex_so) ){

                    dialog_chung(0, getActivity(), "S??? ti???n ph???i d???ng nh???p S???");

                }
                else if (  ngay_chi.isEmpty() ){

                    dialog_chung(0, getActivity(), "Ph???i ch???n Ng??y Chi");

                } else if ( so_tien_chi.length() > 10){

                    dialog_chung(0, getActivity(), "S??? ti???n ph???i < 1 t???");

                } else {
                    try {
                        Chi chi = new Chi(
                                ma_chi_tieu ,
                                userName ,
                                so_tien_chi ,
                                ngay_chi ,
                                chu_Thich
                        );

                        if ( chiDAO.update_Khoan_Chi( chi ) > 0) {

                            dialog_chung(1, getActivity(), "C???p Nh???t Kho???n Chi Th??nh C??ng");
                            dialog.dismiss();

                            list_CHi.clear();
                            list_CHi = chiDAO.getAll_Khoan_Chi();
                            ChiAdapter adapter = new ChiAdapter( getActivity() , list_CHi);
                            lv_ds_Chi.setAdapter(adapter);

                        } else {

                            dialog_chung(1, getActivity(), "C???p Nh???t Th???t B???i");
                        }

                    } catch (Exception ex) {
                        Log.e("Error ????ng K??  : \t\t", ex.toString());
                    }
                }

            }
        });

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                list_CHi.clear();
                list_CHi = chiDAO.getAll_Khoan_Chi();
                ChiAdapter adapter = new ChiAdapter( getActivity() , list_CHi);
                lv_ds_Chi.setAdapter(adapter);
            }
        });

        dialog.show();
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private Integer get_vi_tri(List<NguoiDung> nd , String user){
        for (int i = 0; i < nd.size() ; i++) {
            if ( nd.get(i).getUserName().equals( user ) ){
                return i;
            }
        }
        return 0;
    }

}