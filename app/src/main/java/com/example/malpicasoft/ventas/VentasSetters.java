package com.example.malpicasoft.ventas;

class VentasSetters {

    private String fechaVenta, fechaIngreso, nroFactura, codigoCliente, nombreCliente, condicionCliente,
            codigoProd, descripcionProd, cantidadProd, monedaProd, precioUnitProd, impuestos, precioTotalProd;

    String getFechaVenta() { return fechaVenta; }

    void setFechaVenta(String fechaVenta) { this.fechaVenta = fechaVenta; }

    String getFechaIngreso() { return fechaIngreso; }

    void setFechaIngreso(String fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    String getNroFactura() { return nroFactura; }

    void setNroFactura(String nroFactura) { this.nroFactura = nroFactura; }

    String getCodigoCliente() { return codigoCliente; }

    void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    String getNombreCliente() { return nombreCliente; }

    void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    String getCondicionCliente() { return condicionCliente; }

    void setCondicionCliente(String condicionCliente) {
        this.condicionCliente = condicionCliente;
    }

    String getCodigoProd() {
        return codigoProd;
    }

    void setCodigoProd(String codigoProd) {
        this.codigoProd = codigoProd;
    }

    String getDescripcionProd() {
        return descripcionProd;
    }

    void setDescripcionProd(String descripcionProd) { this.descripcionProd = descripcionProd; }

    String getCantidadProd() { return cantidadProd; }

    void setCantidadProd(String cantidadProd) { this.cantidadProd = cantidadProd; }

    String getMonedaProd() {
        return monedaProd;
    }

    void setMonedaProd(String monedaProd) {
        this.monedaProd = monedaProd;
    }

    String getPrecioUnitProd() {
        return precioUnitProd;
    }

    void setPrecioUnitProd(String precioUnitProd) {
        this.precioUnitProd = precioUnitProd;
    }

    String getImpuestos() {
        return impuestos;
    }

    void setImpuestos(String impuestos) {
        this.impuestos = impuestos;
    }

    String getPrecioTotalProd() {
        return precioTotalProd;
    }

    void setPrecioTotalProd(String precioTotalProd) { this.precioTotalProd = precioTotalProd; }
}
