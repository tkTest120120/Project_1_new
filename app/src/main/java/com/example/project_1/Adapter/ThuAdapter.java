package com.example.project_1.Adapter;

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

import com.example.project_1.DAO.ThuDAO;
import com.example.project_1.Models.Thu;
import com.example.project_1.R;

import java.util.ArrayList;
import java.util.List;

public class ThuAdapter extends BaseAdapter {
    Context context;
    List<Thu> list = new ArrayList<>();
    ThuDAO thuDAO;

    public ThuAdapter(Context context, List<Thu> list) {
        super();
        this.context = context;
        this.list = list;
        thuDAO = new ThuDAO(context);
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
            convertView = inflater.inflate(R.layout.show_chung , null);

            viewHoler.ma_thu_nhap = convertView.findViewById(R.id.tv_chung_ma);
            viewHoler.userName = convertView.findViewById(R.id.tv_chung_tk);
            viewHoler.so_Tien_thu = convertView.findViewById(R.id.tv_chung_so_tien);
            viewHoler.ngay_nhan_tien = convertView.findViewById(R.id.tv_chung_ngay);
            viewHoler.chu_THich = convertView.findViewById(R.id.tv_chung_chu_thich);
            viewHoler.edit = convertView.findViewById(R.id.img_chung_edit);
            viewHoler.delete = convertView.findViewById(R.id.img_chung_delete);

            ((TextView) convertView.findViewById(R.id.tv_chung_so_1)).setText("T??i Kho???n :");
            ((TextView) convertView.findViewById(R.id.tv_chung_so_2)).setText("S??? Ti???n thu :");
            ((TextView) convertView.findViewById(R.id.tv_chung_so_3)).setText("Ng??y Nh???n Ti???n :");
            ((TextView) convertView.findViewById(R.id.tv_chung_so_4)).setText("Ghi ch?? :");

            viewHoler.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Th??ng b??o").setMessage("B???n c?? ch???c ch???n mu???n x??a ng?????i d??ng n??y kh??ng ?");
                    builder.setNegativeButton("X??a",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    thuDAO.delete_khoan_thu_By_ID( list.get(position).getMaThuNhap() );
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "???? x??a Kho???n Thu th??nh c??ng",
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

        viewHoler.ma_thu_nhap.setText( list.get(position).getMaThuNhap() );
        viewHoler.userName.setText( list.get(position).getUserName() );
        viewHoler.so_Tien_thu.setText( list.get(position).getSoTienThu() +" $");
        viewHoler.ngay_nhan_tien.setText( list.get(position).getNgayNhanTien() );
        viewHoler.chu_THich.setText( list.get(position).getChuThich() );

        return convertView;
    }

    private static class ViewHoler{
        TextView ma_thu_nhap;
        TextView userName;
        TextView so_Tien_thu;
        TextView ngay_nhan_tien;
        TextView chu_THich;
        ImageView delete;
        ImageView edit;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }
}
