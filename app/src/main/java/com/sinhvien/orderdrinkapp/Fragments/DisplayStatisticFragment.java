package com.sinhvien.orderdrinkapp.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.sinhvien.orderdrinkapp.Activities.DetailStatisticActivity;
import com.sinhvien.orderdrinkapp.Activities.HomeActivity;
import com.sinhvien.orderdrinkapp.CustomAdapter.AdapterDisplayStatistic;
import com.sinhvien.orderdrinkapp.DAO.DonDatDAO;
import com.sinhvien.orderdrinkapp.DTO.DonDatDTO;
import com.sinhvien.orderdrinkapp.R;

import java.util.List;

public class DisplayStatisticFragment extends Fragment {

    private ListView lvStatistic;
    private List<DonDatDTO> donDatDTOS;
    private DonDatDAO donDatDAO;
    private AdapterDisplayStatistic adapterDisplayStatistic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.displaystatistic_layout, container, false);
        ((HomeActivity) requireActivity()).getSupportActionBar().setTitle("Quản lý thống kê");
        setHasOptionsMenu(true);

        lvStatistic = view.findViewById(R.id.lvStatistic);
        donDatDAO = new DonDatDAO(requireActivity());

        donDatDTOS = donDatDAO.LayDSDonDat();
        adapterDisplayStatistic = new AdapterDisplayStatistic(requireActivity(), R.layout.custom_layout_displaystatistic, donDatDTOS);
        lvStatistic.setAdapter(adapterDisplayStatistic);
        adapterDisplayStatistic.notifyDataSetChanged();

        lvStatistic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DonDatDTO selectedDonDat = donDatDTOS.get(position);
                openDetailStatisticActivity(selectedDonDat);
            }
        });

        return view;
    }

    private void openDetailStatisticActivity(DonDatDTO selectedDonDat) {
        Intent intent = new Intent(requireActivity(), DetailStatisticActivity.class);
        intent.putExtra("madon", selectedDonDat.getMaDonDat());
        intent.putExtra("manv", selectedDonDat.getMaNV());
        intent.putExtra("maban", selectedDonDat.getMaBan());
        intent.putExtra("ngaydat", selectedDonDat.getNgayDat());
        intent.putExtra("tongtien", selectedDonDat.getTongTien());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_display_statistic, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_calculate_total) {
            showTotalRevenueDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTotalRevenueDialog() {
        double totalRevenue = calculateTotalRevenue();
        String message = "Tổng tiền doanh thu là: " + totalRevenue + " VNĐ";

        // Hiển thị hộp thoại thông báo
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Tổng Tiền Doanh Thu")
                .setMessage(message)
                .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng hộp thoại nếu người dùng nhấn Đóng
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private double calculateTotalRevenue() {
        double totalRevenue = 0;
        for (DonDatDTO donDatDTO : donDatDTOS) {
            if ("true".equals(donDatDTO.getTinhTrang())) {
                totalRevenue += Double.parseDouble(donDatDTO.getTongTien());
            }
        }
        return totalRevenue;
    }
}
