package teamb.w4e.cli.model;

public class CliCard {

        private Long id;

        public CliCard() {
        }

        public CliCard(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Card{" +
                    "id='" + id + '\'' +
                    '}';
        }
}
