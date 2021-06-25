package filter;

import java.util.List;

public class FileWithOneInvoiceLineBuilder {
        private final String emptyField = "";
        private String headerLine;
        private String ivaTax = "19";
        private String igicTax = emptyField;
        private String concept = "irrelevant";
        private String netAmount = "810";
        private String cif = "B76430134";
        private String nif = emptyField;

        public FileWithOneInvoiceLineBuilder withHeader(String headerLine) {
            this.headerLine = headerLine;
            return this;
        }

        public FileWithOneInvoiceLineBuilder withIvaTax(String ivaTax) {
            this.ivaTax = ivaTax;
            return this;
        }

        public FileWithOneInvoiceLineBuilder withIgicTax(String igicTax) {
            this.igicTax = igicTax;
            return this;
        }

        public FileWithOneInvoiceLineBuilder withConcept(String concept) {
            this.concept = concept;
            return this;
        }

        public FileWithOneInvoiceLineBuilder withNetValue(String netAmount) {
            this.netAmount = netAmount;
            return this;
        }

        public FileWithOneInvoiceLineBuilder withCif(String cif) {
            this.cif = cif;
            return this;
        }

        public FileWithOneInvoiceLineBuilder withNif(String nif) {
            this.nif = nif;
            return this;
        }


        public List<String> generateLines() {
            final String invoiceId = "1";
            final String invoiceDate = "02/05/2019";
            final String grossAmount = "1000";
            var formattedLine = String.join(",",
                    List.of(
                            invoiceId,
                            invoiceDate,
                            grossAmount,
                            this.netAmount,
                            this.ivaTax,
                            this.igicTax,
                            this.concept,
                            this.cif,
                            this.nif
                    ));
            return List.of(headerLine, formattedLine);
        }
    }
