package com.example.project_1.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_1.DAO.NguoiDungDAO;
import com.example.project_1.Models.NguoiDung;
import com.example.project_1.R;

import java.util.ArrayList;
import java.util.List;

public class DSnguoiDungAdapter extends BaseAdapter {
    Context context;
    List<NguoiDung> list = new ArrayList<>();
    NguoiDungDAO nguoiDungDAO;

    public DSnguoiDungAdapter(Context context, List<NguoiDung> list) {
        super();
        this.context = context;
        this.list = list;
        nguoiDungDAO = new NguoiDungDAO(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler;
        if (convertView == null){
            viewHoler = new ViewHoler();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.show_ds_nguoi_dung , null);

            viewHoler.userName = convertView.findViewById(R.id.tv_ds_tk);
            viewHoler.passWord = convertView.findViewById(R.id.tv_ds_mk);
            viewHoler.hoTen = convertView.findViewById(R.id.tv_ds_hoTen);
            viewHoler.sdt = convertView.findViewById(R.id.tv_ds_sdt);
            viewHoler.delete = convertView.findViewById(R.id.imv_ds_xoa);

            viewHoler.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Th??ng b??o").setMessage("B???n c?? ch???c ch???n mu???n x??a ng?????i d??ng n??y kh??ng ?");
                    builder.setNegativeButton("X??a",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    nguoiDungDAO.deleteNguoiDungByID( list.get(position).getUserName() );
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "???? x??a ng?????i d??ng th??nh c??ng",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                    builder.setPositiveButton("H???y", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHoler) convertView.getTag();
        }
        viewHoler.userName.setText("T??i kho???n :  " + list.get(position).getUserName() );
        viewHoler.passWord.setText("M???t Kh???u :  " + list.get(position).getPassword() );
        viewHoler.hoTen.setText("H??? t??n :  " + list.get(position).getHoTen() );
        viewHoler.sdt.setText("S??? ??i???n Tho???i :  " + list.get(position).getPhone() );
        return convertView;
    }

    private static class ViewHoler{
        TextView userName;
        TextView passWord;
        TextView hoTen;
        TextView sdt;
        ImageView delete;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
